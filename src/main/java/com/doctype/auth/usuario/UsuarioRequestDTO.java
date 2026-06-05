package com.doctype.auth.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    @NotBlank(message = "Nome não pode estar vazio")
    private String nome;

    private String nomeUsuario;

    private String telefone;

    private String cpf;

    private String endereco;

    private String cep;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email não pode estar vazio")
    private String email;

    @NotBlank(message = "Senha não pode estar vazia")
    private String senha;

    private String perfilAcesso;
}