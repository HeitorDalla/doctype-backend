package com.doctype.auth.documento;

import com.doctype.auth.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "documentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String protocolo;

    @Column(nullable = false)
    private String nome;

    @Column(length = 2000)
    private String descricao;

    @Column(name = "tipo_arquivo")
    private String tipoArquivo;

    @Column(nullable = false)
    private String status;

    @Column(name = "arquivo_nome")
    private String arquivoNome;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "arquivo_dados", columnDefinition = "LONGBLOB")
    @ToString.Exclude
    private byte[] arquivoDados;

    @Column(name = "arquivo_content_type")
    private String arquivoContentType;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @ToString.Exclude
    private Usuario usuario;

    @PrePersist
    protected void onCreate() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }
}