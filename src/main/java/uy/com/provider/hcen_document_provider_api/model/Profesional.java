package uy.com.provider.hcen_document_provider_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "profesional", schema = "clinica_c")
@Data
public class Profesional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profesionalId;
    private String nombreUsuario;
    private String passwordHash;
    private String nombre;
    private String apellido;
    private String email;
    private String estado;
    private String especializacion;
}
