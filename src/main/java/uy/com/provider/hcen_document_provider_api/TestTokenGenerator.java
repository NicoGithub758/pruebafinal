package uy.com.provider.hcen_document_provider_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uy.com.provider.hcen_document_provider_api.security.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class TestTokenGenerator implements CommandLineRunner {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void run(String... args) throws Exception {
        // Datos exactos para simular una llamada del sistema HCEN a la 'clinica_a'
        String subject = "hcen_system_service";
        String tenantId = "prestador_externo";
        String role = "SYSTEM";
        
        // Generamos el token
        String testToken = jwtTokenProvider.generateToken(subject, tenantId, role);

        // Imprimimos el token en la consola de forma muy visible
        System.out.println("\n\n====================================================================================");
        System.out.println("==============           TOKEN DE PRUEBA PARA POSTMAN           ==============");
        System.out.println("====================================================================================\n");
        System.out.println(testToken);
        System.out.println("\n====================================================================================\n");
    }
}