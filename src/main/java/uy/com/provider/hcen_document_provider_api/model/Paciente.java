package uy.com.provider.hcen_document_provider_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paciente", schema = "prestador_externo")
@Data
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pacienteId;
    private Long globaluserId;
    private String nroDocumento;
    private String nombre;
    private String apellido;
    private String email;
    private String sexo;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}