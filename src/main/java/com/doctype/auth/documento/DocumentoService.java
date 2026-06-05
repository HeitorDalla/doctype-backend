package com.doctype.auth.documento;

import com.doctype.auth.entity.Usuario;
import com.doctype.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<DocumentoResponseDTO> listar(String termo, String status, String tipo, String protocolo, String nome) {
        return documentoRepository.findAllByOrderByCriadoEmDesc().stream()
                .filter(documento -> filtrarTexto(documento.getNome(), nome))
                .filter(documento -> filtrarTexto(documento.getProtocolo(), protocolo))
                .filter(documento -> filtrarTexto(documento.getStatus(), status))
                .filter(documento -> filtrarTexto(documento.getTipoArquivo(), tipo))
                .filter(documento -> filtrarTexto(documento.getNome(), termo)
                        || filtrarTexto(documento.getDescricao(), termo)
                        || filtrarTexto(documento.getProtocolo(), termo))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<DocumentoResponseDTO> listarPorUsuarioEmail(String email) {
        return documentoRepository.findByUsuarioEmailOrderByCriadoEmDesc(email).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DocumentoResponseDTO criar(DocumentoRequestDTO request) {
        return criar(request, null);
    }

    public DocumentoResponseDTO criar(DocumentoRequestDTO request, MultipartFile arquivo) {
        Usuario usuario = null;
        if (request.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(request.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        }

        String arquivoNome = defaultString(request.getArquivoNome(), arquivo != null ? arquivo.getOriginalFilename() : null);
        String arquivoContentType = defaultString(request.getArquivoContentType(), arquivo != null ? arquivo.getContentType() : null);
        byte[] arquivoDados = null;

        if (arquivo != null && !arquivo.isEmpty()) {
            try {
                arquivoDados = arquivo.getBytes();
            } catch (IOException exception) {
                throw new RuntimeException("Falha ao processar arquivo enviado", exception);
            }
        }

        Documento documento = Documento.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .tipoArquivo(defaultString(request.getTipoArquivo(), "PDF"))
                .status(defaultString(request.getStatus(), "Em Análise"))
                .arquivoNome(arquivoNome)
                .arquivoContentType(arquivoContentType)
                .arquivoDados(arquivoDados)
                .usuario(usuario)
                .build();

        return toResponse(documentoRepository.save(documento));
    }

    public DocumentoResponseDTO buscarPorProtocolo(String protocolo) {
        return documentoRepository.findByProtocolo(protocolo)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));
    }

    public DocumentoResponseDTO buscarPorId(Long id) {
        return documentoRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));
    }

    public List<DocumentoResponseDTO> filtrarPorData(LocalDate data) {
        return documentoRepository.findAllByOrderByCriadoEmDesc().stream()
                .filter(documento -> documento.getCriadoEm() != null && documento.getCriadoEm().toLocalDate().equals(data))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private DocumentoResponseDTO toResponse(Documento documento) {
        Usuario usuario = documento.getUsuario();
        return DocumentoResponseDTO.builder()
                .id(documento.getId())
                .protocolo(documento.getProtocolo())
                .nome(documento.getNome())
                .descricao(documento.getDescricao())
                .tipoArquivo(documento.getTipoArquivo())
                .status(documento.getStatus())
                .arquivoNome(documento.getArquivoNome())
                .arquivoContentType(documento.getArquivoContentType())
                .criadoEm(documento.getCriadoEm())
                .usuarioId(usuario != null ? usuario.getId() : null)
                .usuarioNome(usuario != null ? usuario.getNome() : null)
                .usuarioEmail(usuario != null ? usuario.getEmail() : null)
                .build();
    }

    private boolean filtrarTexto(String valor, String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return true;
        }

        if (valor == null) {
            return false;
        }

        return valor.toLowerCase(Locale.ROOT).contains(filtro.toLowerCase(Locale.ROOT));
    }

    private String defaultString(String valor, String fallback) {
        return valor == null || valor.isBlank() ? fallback : valor;
    }
}