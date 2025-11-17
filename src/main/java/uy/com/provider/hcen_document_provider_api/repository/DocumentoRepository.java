package uy.com.provider.hcen_document_provider_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uy.com.provider.hcen_document_provider_api.model.DocumentoClinico;
import java.util.Optional;

public interface DocumentoRepository extends JpaRepository<DocumentoClinico, Long> {

    @Query("SELECT d FROM DocumentoClinico d JOIN FETCH d.paciente JOIN FETCH d.profesional WHERE d.idExternaDoc = :idExternaDoc")
    Optional<DocumentoClinico> findByIdExternaDocWithDetails(String idExternaDoc);
}