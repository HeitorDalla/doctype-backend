package com.doctype.auth.documento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoResponseDTO {

    private Long id;
    private String protocolo;
    private String nome;
    private String descricao;
    private String tipoArquivo;
    private String status;
    private String arquivoNome;
    private String arquivoContentType;
    private LocalDateTime criadoEm;
    private Long usuarioId;
    private String usuarioNome;
    private String usuarioEmail;
}