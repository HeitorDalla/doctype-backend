package com.doctype.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String nome;

    @Column(name = "nome_usuario", unique = true)
    private String nomeUsuario;

    private String telefone;

    private String cpf;

    private String endereco;

    private String cep;

    @Column(name = "perfil_acesso")
    private String perfilAcesso;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Long dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = System.currentTimeMillis();
    }

}
