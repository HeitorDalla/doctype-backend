package com.doctype.auth.documento;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoRequestDTO {

    @NotBlank(message = "Nome não pode estar vazio")
    private String nome;

    private String descricao;

    private String tipoArquivo;

    private String status;

    private String arquivoNome;

    private String arquivoContentType;

    private Long usuarioId;
}