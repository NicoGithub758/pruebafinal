package uy.com.provider.hcen_document_provider_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uy.com.provider.hcen_document_provider_api.dto.DocumentoClinicoDTO;
import uy.com.provider.hcen_document_provider_api.service.DocumentoService;

@RestController
@RequestMapping("/{tenantId}/api/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    @GetMapping("/{idExternaDoc}")
    public ResponseEntity<DocumentoClinicoDTO> getDocumentoByIdExterna(
            @PathVariable String tenantId,
            @PathVariable String idExternaDoc) {
        
        // El TenantContext ya fue establecido por el filtro de seguridad, 
        // pero pasamos el tenantId explícitamente al servicio por claridad.
        DocumentoClinicoDTO documento = documentoService.findByIdExterna(idExternaDoc, tenantId);
        
        return ResponseEntity.ok(documento);
    }
}