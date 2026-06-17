package com.doctype.auth.tipodocumento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    public List<TipoDocumento> listarAtivos() {
        return tipoDocumentoRepository.findByAtivoTrueOrderByNomeAsc();
    }

    public TipoDocumento criar(TipoDocumento request) {
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do tipo de documento é obrigatório");
        }

        if (tipoDocumentoRepository.existsByNomeIgnoreCase(request.getNome().trim())) {
            throw new IllegalArgumentException("Tipo de documento já cadastrado");
        }

        TipoDocumento tipoDocumento = TipoDocumento.builder()
                .nome(request.getNome().trim())
                .descricao(request.getDescricao())
                .ativo(Boolean.TRUE)
                .build();

        return tipoDocumentoRepository.save(tipoDocumento);
    }
}
