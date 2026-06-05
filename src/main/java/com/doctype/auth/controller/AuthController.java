package com.doctype.auth.controller;

import com.doctype.auth.dto.LoginRequestDTO;
import com.doctype.auth.dto.LoginResponseDTO;
import com.doctype.auth.dto.RegistroRequestDTO;
import com.doctype.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            log.info("Recebida requisição de login para: {}", request.getEmail());
            LoginResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Erro no login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO("Falha na autenticação", e.getMessage()));
        } catch (Exception e) {
            log.error("Erro inesperado no login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Erro interno", "Erro ao processar login"));
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody RegistroRequestDTO request) {
        try {
            log.info("Recebida requisição de registro para: {}", request.getEmail());
            LoginResponseDTO response = authService.registro(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Erro no registro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO("Falha no registro", e.getMessage()));
        } catch (Exception e) {
            log.error("Erro inesperado no registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Erro interno", "Erro ao processar registro"));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken() {
        log.info("Token validado com sucesso");
        return ResponseEntity.ok(new MessageResponseDTO("Token válido"));
    }

}
