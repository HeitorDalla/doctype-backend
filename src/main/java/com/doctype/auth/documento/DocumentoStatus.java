package com.doctype.auth.documento;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum DocumentoStatus {

    RECEBIDO("Recebido"),
    EM_ANALISE("Em análise"),
    ENCAMINHADO("Encaminhado"),
    FINALIZADO("Finalizado");

    private final String label;

    DocumentoStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static String normalizarStatus(String status) {
        if (status == null || status.isBlank()) {
            return RECEBIDO.getLabel();
        }

        String normalized = normalize(status);
        return Arrays.stream(values())
                .filter(current -> normalize(current.label).equals(normalized))
                .findFirst()
                .map(DocumentoStatus::getLabel)
                .orElseThrow(() -> new IllegalArgumentException("Status inválido. Permitidos: " + opcoesTexto()));
    }

    public static List<String> opcoes() {
        return Arrays.stream(values()).map(DocumentoStatus::getLabel).toList();
    }

    public static String opcoesTexto() {
        return String.join(", ", opcoes());
    }

    private static String normalize(String value) {
        String withoutAccent = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return withoutAccent
                .toLowerCase(Locale.ROOT)
                .replace('-', ' ')
                .replace('_', ' ')
                .trim()
                .replaceAll("\\s+", " ");
    }
}
