package com.doctype.auth.usuario;

import com.doctype.auth.entity.Usuario;
import com.doctype.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO criar(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já registrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .nomeUsuario(request.getNomeUsuario())
                .telefone(request.getTelefone())
                .cpf(request.getCpf())
                .endereco(request.getEndereco())
                .cep(request.getCep())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .perfilAcesso(defaultPerfil(request.getPerfilAcesso()))
                .ativo(true)
                .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario buscarEntidadePorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(request.getNome());
        usuario.setNomeUsuario(request.getNomeUsuario());
        usuario.setTelefone(request.getTelefone());
        usuario.setCpf(request.getCpf());
        usuario.setEndereco(request.getEndereco());
        usuario.setCep(request.getCep());
        usuario.setPerfilAcesso(defaultPerfil(request.getPerfilAcesso()));

        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email já registrado");
            }
            usuario.setEmail(request.getEmail());
        }

        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        return toResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO toResponse(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .nomeUsuario(usuario.getNomeUsuario())
                .telefone(usuario.getTelefone())
                .cpf(usuario.getCpf())
                .endereco(usuario.getEndereco())
                .cep(usuario.getCep())
                .email(usuario.getEmail())
                .perfilAcesso(usuario.getPerfilAcesso())
                .ativo(usuario.getAtivo())
                .dataCriacao(usuario.getDataCriacao() != null ? Instant.ofEpochMilli(usuario.getDataCriacao()).atZone(ZoneId.systemDefault()).toLocalDateTime() : null)
                .build();
    }

    private String defaultPerfil(String perfilAcesso) {
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