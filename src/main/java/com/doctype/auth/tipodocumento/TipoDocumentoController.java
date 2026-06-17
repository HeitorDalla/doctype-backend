package com.doctype.auth.tipodocumento;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-documento")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    @GetMapping
    public ResponseEntity<List<TipoDocumento>> listarAtivos() {
        return ResponseEntity.ok(tipoDocumentoService.listarAtivos());
    }

    @PostMapping
    public ResponseEntity<TipoDocumento> criar(@RequestBody TipoDocumento request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoDocumentoService.criar(request));
    }
}
