package org.sistemas.ticketsystemapp.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*") // Para que no te dé problemas de CORS con React
public class CiscoTestController {

    @PostMapping("/dial")
    public ResponseEntity<Map<String, String>> testDial() {
        // !! CONFIGURA ESTOS 4 DATOS CON TU ESCENARIO REAL !!
        String phoneIp = "172.30.101.175";     // La IP que anotaste de tu teléfono Cisco
        String targetNumber = "1023";          // El número/extensión al que quieres marcar para probar
        String webUser = "admin";             // Usuario web del teléfono (deja vacío "" si no pide)
        String webPassword = "admin";         // Contraseña web del teléfono (deja vacío "" si no pide)

        String url = "http://" + phoneIp + "/CGI/Execute";

        // El payload XML nativo que entiende el micro-servidor de Cisco
        String xmlPayload = "<CiscoIPPhoneExecute>" +
                "  <ExecuteItem URL=\"Dial:" + targetNumber + "\"/>" +
                "</CiscoIPPhoneExecute>";

        Map<String, String> response = new HashMap<>();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);

            // Si el teléfono requiere autenticación web, añadimos el header
            if (!webUser.isEmpty()) {
                String auth = webUser + ":" + webPassword;
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
                headers.set("Authorization", "Basic " + encodedAuth);
            }

            HttpEntity<String> request = new HttpEntity<>(xmlPayload, headers);
            RestTemplate restTemplate = new RestTemplate();

            // Enviamos el POST al teléfono
            ResponseEntity<String> phoneResponse = restTemplate.postForEntity(url, request, String.class);

            response.put("status", "SUCCESS");
            response.put("phone_raw_response", phoneResponse.getBody());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
