package com.doctype.auth.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumoDashboardDTO {

    private long totalUsuarios;
    private long totalDocumentos;
    private long documentosEmAnalise;
    private long documentosEncaminhados;
    private long documentosAprovados;
    private long documentosRejeitados;
    private long documentosFinalizados;
}