package uy.com.provider.hcen_document_provider_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class DocumentoClinicoDTO {
    private PacienteInfo paciente;
    private ProfesionalInfo profesional;
    private String instanciaMedica;
    private String fechaAtencion;
    private String lugar;
    private String idExternaDoc;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaGeneracion;
    private String custodio;
    private List<MotivoConsultaDTO> motivosDeConsulta;
    private List<DiagnosticoDTO> diagnosticos;
    private List<InstruccionSeguimientoDTO> instruccionesDeSeguimiento;

    // --- Clases anidadas para la estructura JSON ---
    @Data @NoArgsConstructor public static class PacienteInfo {
        private String nombreCompleto;
        private String nroDocumento;
        private String fechaNacimiento;
        private String sexo;
    }
    @Data @NoArgsConstructor public static class ProfesionalInfo {
        private String nombreCompleto;
    }
    @Data @NoArgsConstructor public static class MotivoConsultaDTO {
        private String descripcion;
    }
    @Data @NoArgsConstructor public static class DiagnosticoDTO {
        private String descripcion;
        private String fechaInicio;
        private String estadoProblema;
        private String gradoCerteza;
    }
    @Data @NoArgsConstructor public static class InstruccionSeguimientoDTO {
        private String tipo;
        private String descripcion;
    }
}