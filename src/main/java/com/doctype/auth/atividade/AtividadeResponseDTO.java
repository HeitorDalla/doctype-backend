package com.doctype.auth.atividade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtividadeResponseDTO {

    private Long id;
    private String descricao;
    private LocalDateTime criadoEm;
    private Long usuarioId;
    private String usuarioNome;
    private Long documentoId;
    private String documentoNome;
}