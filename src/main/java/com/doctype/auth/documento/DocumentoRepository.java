package com.doctype.auth.documento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    Optional<Documento> findByProtocolo(String protocolo);

    List<Documento> findAllByOrderByCriadoEmDesc();

    List<Documento> findByUsuarioEmailOrderByCriadoEmDesc(String email);
}