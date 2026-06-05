package com.doctype.auth.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String nomeUsuario;
    private String telefone;
    private String cpf;
    private String endereco;
    private String cep;
    private String email;
    private String perfilAcesso;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
}