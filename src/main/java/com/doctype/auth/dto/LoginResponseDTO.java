package com.doctype.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private Long id;
    private String email;
    private String nome;
    private String perfilAcesso;
    private String token;
    private String tokenType = "Bearer";
    private Long expiresIn;

}
