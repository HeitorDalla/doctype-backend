package com.doctype.auth.service;

import com.doctype.auth.dto.LoginRequestDTO;
import com.doctype.auth.dto.LoginResponseDTO;
import com.doctype.auth.dto.RegistroRequestDTO;
import com.doctype.auth.entity.Usuario;
import com.doctype.auth.repository.UsuarioRepository;
import com.doctype.auth.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Slf4j
@Transactional
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Tentativa de login para email: {}", request.getEmail());

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            log.warn("Senha incorreta para usuário: {}", request.getEmail());
            throw new RuntimeException("Email ou senha inválidos");
        }

        if (!usuario.getAtivo()) {
            log.warn("Tentativa de login com usuário inativo: {}", request.getEmail());
            throw new RuntimeException("Usuário inativo");
        }

        String token = jwtTokenProvider.generateToken(usuario.getEmail(), usuario.getId());
        log.info("Login bem-sucedido para: {}", request.getEmail());

        return LoginResponseDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nome(usuario.getNome())
            .perfilAcesso(usuario.getPerfilAcesso())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpirationInMillis())
                .build();
    }

    public LoginResponseDTO registro(RegistroRequestDTO request) {
        log.info("Tentativa de registro para email: {}", request.getEmail());

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            log.warn("Email já existe: {}", request.getEmail());
            throw new RuntimeException("Email já registrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
            .perfilAcesso(normalizarPerfil(request.getPerfilAcesso()))
                .ativo(true)
                .build();

        usuario = usuarioRepository.save(usuario);
        log.info("Novo usuário registrado: {}", request.getEmail());

        String token = jwtTokenProvider.generateToken(usuario.getEmail(), usuario.getId());

        return LoginResponseDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nome(usuario.getNome())
            .perfilAcesso(usuario.getPerfilAcesso())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpirationInMillis())
                .build();
    }

    private String normalizarPerfil(String perfilAcesso) {
        if (perfilAcesso == null || perfilAcesso.isBlank()) {
            return "OPERADOR";
        }

        String perfil = perfilAcesso.trim().toUpperCase(Locale.ROOT);
        if (!"ADMINISTRADOR".equals(perfil) && !"OPERADOR".equals(perfil)) {
            throw new RuntimeException("Perfil inválido. Permitidos: ADMINISTRADOR, OPERADOR");
        }

        return perfil;
    }

}
