package com.doctype.auth.relatorio;

import com.doctype.auth.documento.DocumentoResponseDTO;
import com.doctype.auth.documento.DocumentoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final DocumentoService documentoService;

    public RelatorioController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @GetMapping("/documentos")
    public ResponseEntity<List<DocumentoResponseDTO>> documentos(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String protocolo,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        if (data != null) {
            return ResponseEntity.ok(documentoService.filtrarPorData(data));
        }

        return ResponseEntity.ok(documentoService.listar(termo, status, tipo, protocolo, nome));
    }
}