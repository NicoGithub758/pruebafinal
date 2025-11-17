package uy.com.provider.hcen_document_provider_api.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "documento_clinico", schema = "prestador_externo")
@Data
public class DocumentoClinico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;
    
    @Column(unique = true, nullable = false)
    private String idExternaDoc;
    
    private String instanciaMedica;
    private String lugar;
    private LocalDateTime fechaAtencion;
    private LocalDateTime fechaGeneracion;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private String motivos;

    @JdbcTypeCode(SqlTypes.JSON)
    private String diagnosticos;

    @JdbcTypeCode(SqlTypes.JSON)
    private String instrucciones;
}