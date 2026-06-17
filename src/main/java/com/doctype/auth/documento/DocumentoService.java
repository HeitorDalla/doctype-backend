package com.doctype.auth.documento;

import com.doctype.auth.atividade.AtividadeResponseDTO;
import com.doctype.auth.atividade.AtividadeService;
import com.doctype.auth.entity.Usuario;
import com.doctype.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AtividadeService atividadeService;

    public List<DocumentoResponseDTO> listar(
        String termo,
        String status,
        String tipo,
        String protocolo,
        String nome,
        String remetente,
        LocalDate dataInicio,
        LocalDate dataFim
    ) {
    String statusNormalizado = status == null || status.isBlank() ? null : DocumentoStatus.normalizarStatus(status);

        return documentoRepository.findAllByOrderByCriadoEmDesc().stream()
                .filter(documento -> filtrarTexto(documento.getNome(), nome))
                .filter(documento -> filtrarTexto(documento.getProtocolo(), protocolo))
        .filter(documento -> filtrarTexto(documento.getStatus(), statusNormalizado))
                .filter(documento -> filtrarTexto(documento.getTipoArquivo(), tipo))
        .filter(documento -> filtrarRemetente(documento, remetente))
        .filter(documento -> filtrarPeriodo(documento, dataInicio, dataFim))
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

    public DocumentoResponseDTO criar(DocumentoRequestDTO request, String usuarioAutenticadoEmail) {
        return criar(request, null, usuarioAutenticadoEmail);
    }

    public DocumentoResponseDTO criar(DocumentoRequestDTO request, MultipartFile arquivo, String usuarioAutenticadoEmail) {
        Usuario usuario = null;
        if (request.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(request.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        } else if (usuarioAutenticadoEmail != null && !usuarioAutenticadoEmail.isBlank()) {
            usuario = usuarioRepository.findByEmail(usuarioAutenticadoEmail)
                    .orElse(null);
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
        .protocolo(gerarProtocoloUnico())
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .tipoArquivo(defaultString(request.getTipoArquivo(), "PDF"))
        .status(DocumentoStatus.normalizarStatus(request.getStatus()))
                .arquivoNome(arquivoNome)
                .arquivoContentType(arquivoContentType)
                .arquivoDados(arquivoDados)
                .usuario(usuario)
                .build();

    Documento salvo = documentoRepository.save(documento);

    atividadeService.registrarMovimentacao(
        salvo,
        usuario,
        "Documento registrado com status " + salvo.getStatus()
    );

    return toResponse(salvo);
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

    public DocumentoResponseDTO atualizarStatus(Long id, String novoStatus, String usuarioAutenticadoEmail) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        String statusNormalizado = DocumentoStatus.normalizarStatus(novoStatus);
        String statusAnterior = documento.getStatus();
        documento.setStatus(statusNormalizado);

        Usuario usuarioResponsavel = null;
        if (usuarioAutenticadoEmail != null && !usuarioAutenticadoEmail.isBlank()) {
            usuarioResponsavel = usuarioRepository.findByEmail(usuarioAutenticadoEmail).orElse(null);
        }

        Documento salvo = documentoRepository.save(documento);

        atividadeService.registrarMovimentacao(
                salvo,
                usuarioResponsavel,
                "Status alterado de " + statusAnterior + " para " + statusNormalizado
        );

        return toResponse(salvo);
    }

    public List<AtividadeResponseDTO> listarHistorico(Long documentoId) {
        return atividadeService.listarPorDocumento(documentoId);
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

    private boolean filtrarRemetente(Documento documento, String remetente) {
        if (remetente == null || remetente.isBlank()) {
            return true;
        }

        if (documento.getUsuario() == null) {
            return false;
        }

        return filtrarTexto(documento.getUsuario().getNome(), remetente)
                || filtrarTexto(documento.getUsuario().getEmail(), remetente);
    }

    private boolean filtrarPeriodo(Documento documento, LocalDate dataInicio, LocalDate dataFim) {
        if (documento.getCriadoEm() == null) {
            return false;
        }

        if (dataInicio != null && documento.getCriadoEm().isBefore(dataInicio.atStartOfDay())) {
            return false;
        }

        return dataFim == null || !documento.getCriadoEm().isAfter(dataFim.atTime(LocalTime.MAX));
    }

    private String gerarProtocoloUnico() {
        String protocolo;
        do {
            String sufixo = UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
            protocolo = "PRT-" + System.currentTimeMillis() + "-" + sufixo;
        } while (documentoRepository.existsByProtocolo(protocolo));

        return protocolo;
    }

    private String defaultString(String valor, String fallback) {
        return valor == null || valor.isBlank() ? fallback : valor;
    }
}