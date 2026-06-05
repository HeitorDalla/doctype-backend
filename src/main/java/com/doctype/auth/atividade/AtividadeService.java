package com.doctype.auth.atividade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    public List<AtividadeResponseDTO> listarRecentes(String email) {
        List<Atividade> atividades = email == null || email.isBlank()
                ? atividadeRepository.findTop10ByOrderByCriadoEmDesc()
                : atividadeRepository.findTop10ByUsuarioEmailOrderByCriadoEmDesc(email);

        return atividades.stream()
                .map(atividade -> AtividadeResponseDTO.builder()
                        .id(atividade.getId())
                        .descricao(atividade.getDescricao())
                        .criadoEm(atividade.getCriadoEm())
                        .usuarioId(atividade.getUsuario() != null ? atividade.getUsuario().getId() : null)
                        .usuarioNome(atividade.getUsuario() != null ? atividade.getUsuario().getNome() : null)
                        .documentoId(atividade.getDocumento() != null ? atividade.getDocumento().getId() : null)
                        .documentoNome(atividade.getDocumento() != null ? atividade.getDocumento().getNome() : null)
                        .build())
                .collect(Collectors.toList());
    }
}