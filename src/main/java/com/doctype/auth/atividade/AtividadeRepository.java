package com.doctype.auth.atividade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    List<Atividade> findTop10ByUsuarioEmailOrderByCriadoEmDesc(String email);

    List<Atividade> findTop10ByOrderByCriadoEmDesc();

    List<Atividade> findByDocumentoIdOrderByCriadoEmDesc(Long documentoId);
}