package com.doctype.auth.documento;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusDocumentoRequestDTO {

    @NotBlank(message = "Status é obrigatório")
    private String status;
}
