package com.doctype.auth.atividade;

import com.doctype.auth.documento.Documento;
import com.doctype.auth.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    public List<AtividadeResponseDTO> listarRecentes(String email) {
        List<Atividade> atividades = email == null || email.isBlank()
                ? atividadeRepository.findTop10ByOrderByCriadoEmDesc()
                : atividadeRepository.findTop10ByUsuarioEmailOrderByCriadoEmDesc(email);

        return atividades.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AtividadeResponseDTO> listarPorDocumento(Long documentoId) {
        return atividadeRepository.findByDocumentoIdOrderByCriadoEmDesc(documentoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void registrarMovimentacao(Documento documento, Usuario usuario, String descricao) {
        Atividade atividade = Atividade.builder()
                .descricao(descricao)
                .documento(documento)
                .usuario(usuario)
                .build();

        atividadeRepository.save(atividade);
    }

    private AtividadeResponseDTO toResponse(Atividade atividade) {
        return AtividadeResponseDTO.builder()
                .id(atividade.getId())
                .descricao(atividade.getDescricao())
                .criadoEm(atividade.getCriadoEm())
                .usuarioId(atividade.getUsuario() != null ? atividade.getUsuario().getId() : null)
                .usuarioNome(atividade.getUsuario() != null ? atividade.getUsuario().getNome() : null)
                .documentoId(atividade.getDocumento() != null ? atividade.getDocumento().getId() : null)
                .documentoNome(atividade.getDocumento() != null ? atividade.getDocumento().getNome() : null)
                .build();
    }
}