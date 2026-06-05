package com.doctype.auth.dashboard;

import com.doctype.auth.documento.Documento;
import com.doctype.auth.documento.DocumentoRepository;
import com.doctype.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final DocumentoRepository documentoRepository;

    public ResumoDashboardDTO obterResumo() {
        long totalDocumentos = documentoRepository.count();
        return ResumoDashboardDTO.builder()
                .totalUsuarios(usuarioRepository.count())
                .totalDocumentos(totalDocumentos)
                .documentosEmAnalise(contarPorStatus("Em Análise"))
                .documentosEncaminhados(contarPorStatus("Encaminhado"))
                .documentosAprovados(contarPorStatus("Aprovado"))
                .documentosRejeitados(contarPorStatus("Rejeitado"))
                .documentosFinalizados(contarPorStatus("Finalizado"))
                .build();
    }

    private long contarPorStatus(String status) {
        return documentoRepository.findAll().stream()
                .map(Documento::getStatus)
                .filter(status::equalsIgnoreCase)
                .count();
    }
}