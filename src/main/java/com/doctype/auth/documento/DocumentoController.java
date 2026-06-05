package com.doctype.auth.documento;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
@CrossOrigin(origins = "*")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentoResponseDTO>> listar(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String protocolo,
            @RequestParam(required = false) String nome
    ) {
        return ResponseEntity.ok(documentoService.listar(termo, status, tipo, protocolo, nome));
    }

    @GetMapping("/meus-documentos")
    public ResponseEntity<List<DocumentoResponseDTO>> meusDocumentos(Authentication authentication) {
        return ResponseEntity.ok(documentoService.listarPorUsuarioEmail(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(documentoService.buscarPorId(id));
    }

    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<DocumentoResponseDTO> buscarPorProtocolo(@PathVariable String protocolo) {
        return ResponseEntity.ok(documentoService.buscarPorProtocolo(protocolo));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentoResponseDTO> criar(@RequestBody DocumentoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoService.criar(request));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoResponseDTO> criarComArquivo(
            @RequestParam String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String tipoArquivo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String arquivoNome,
            @RequestParam(required = false) String arquivoContentType,
            @RequestParam(required = false) Long usuarioId,
            @RequestPart(required = false) MultipartFile arquivo
    ) {
        DocumentoRequestDTO request = DocumentoRequestDTO.builder()
                .nome(nome)
                .descricao(descricao)
                .tipoArquivo(tipoArquivo)
                .status(status)
                .arquivoNome(arquivoNome)
                .arquivoContentType(arquivoContentType)
                .usuarioId(usuarioId)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(documentoService.criar(request, arquivo));
    }
}