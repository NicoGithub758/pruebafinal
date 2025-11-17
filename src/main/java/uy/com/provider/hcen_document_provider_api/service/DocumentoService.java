package uy.com.provider.hcen_document_provider_api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uy.com.provider.hcen_document_provider_api.dto.DocumentoClinicoDTO;
import uy.com.provider.hcen_document_provider_api.model.DocumentoClinico;
import uy.com.provider.hcen_document_provider_api.repository.DocumentoRepository;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public DocumentoClinicoDTO findByIdExterna(String idExterna, String tenantId) {
        // Usamos el nuevo método del repositorio que carga todo (paciente y profesional)
        DocumentoClinico doc = repository.findByIdExternaDocWithDetails(idExterna)
            .orElseThrow(() -> new RuntimeException("Documento no encontrado: " + idExterna));
        
        try {
            return mapToDTO(doc, tenantId);
        } catch (Exception e) {
            // Lanza una excepción genérica si falla el mapeo, lo cual ayuda a depurar.
            throw new RuntimeException("Error interno al procesar los datos del documento.", e);
        }
    }

    private DocumentoClinicoDTO mapToDTO(DocumentoClinico doc, String tenantId) throws Exception {
        DocumentoClinicoDTO dto = new DocumentoClinicoDTO();
        
        // --- LÓGICA DE MAPEO ACTUALIZADA ---
        
        // Mapeo desde el objeto Paciente que viene relacionado en la entidad
        DocumentoClinicoDTO.PacienteInfo pacienteInfo = new DocumentoClinicoDTO.PacienteInfo();
        if (doc.getPaciente() != null) {
            pacienteInfo.setNombreCompleto(doc.getPaciente().getNombre() + " " + doc.getPaciente().getApellido());
            pacienteInfo.setNroDocumento(doc.getPaciente().getNroDocumento());
            pacienteInfo.setSexo(doc.getPaciente().getSexo());
            if (doc.getPaciente().getFechaNacimiento() != null) {
                 // Formato estándar ISO 8601 para fechas: YYYY-MM-DD
                 pacienteInfo.setFechaNacimiento(doc.getPaciente().getFechaNacimiento().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        }
        dto.setPaciente(pacienteInfo);

        // Mapeo desde el objeto Profesional que viene relacionado en la entidad
        DocumentoClinicoDTO.ProfesionalInfo profesionalInfo = new DocumentoClinicoDTO.ProfesionalInfo();
        if (doc.getProfesional() != null) {
            profesionalInfo.setNombreCompleto(doc.getProfesional().getNombre() + " " + doc.getProfesional().getApellido());
        }
        dto.setProfesional(profesionalInfo);

        // Mapeo de campos directos del documento y deserialización de campos JSONB
        dto.setInstanciaMedica(doc.getInstanciaMedica());
        dto.setLugar(doc.getLugar());
        dto.setIdExternaDoc(doc.getIdExternaDoc());
        dto.setFechaGeneracion(doc.getFechaGeneracion());
        dto.setCustodio("Clínica B (ID: " + tenantId + ")");
        
        // Formateo de fecha legible para humanos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));
        dto.setFechaAtencion(doc.getFechaAtencion() != null ? doc.getFechaAtencion().format(formatter) : "No especificada");
        
        // Deserialización de los campos JSONB a listas de objetos
        dto.setMotivosDeConsulta(doc.getMotivos() != null ? objectMapper.readValue(doc.getMotivos(), new TypeReference<>() {}) : Collections.emptyList());
        dto.setDiagnosticos(doc.getDiagnosticos() != null ? objectMapper.readValue(doc.getDiagnosticos(), new TypeReference<>() {}) : Collections.emptyList());
        dto.setInstruccionesDeSeguimiento(doc.getInstrucciones() != null ? objectMapper.readValue(doc.getInstrucciones(), new TypeReference<>() {}) : Collections.emptyList());

        return dto;
    }
}