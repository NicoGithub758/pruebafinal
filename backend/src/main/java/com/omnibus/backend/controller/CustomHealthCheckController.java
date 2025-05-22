import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomHealthCheckController {

    @GetMapping("/custom-health") // Elige una ruta simple
    public ResponseEntity<String> healthCheck() {
        // Podrías añadir verificaciones muy básicas aquí si lo deseas,
        // pero mantenlo rápido. Por ejemplo, verificar si un bean esencial está cargado.
        // Pero para la velocidad, un simple OK es suficiente.
        return ResponseEntity.ok("{\"status\":\"CUSTOM_UP\"}");
    }
}