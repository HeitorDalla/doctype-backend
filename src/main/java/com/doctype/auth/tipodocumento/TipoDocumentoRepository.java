package com.doctype.auth.tipodocumento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    Optional<TipoDocumento> findByNomeIgnoreCase(String nome);

    List<TipoDocumento> findByAtivoTrueOrderByNomeAsc();
}
