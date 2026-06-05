package com.doctype.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> apiRoot() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "frontend", "/index.html",
                "auth", "/api/auth/login"
        ));
    }
}