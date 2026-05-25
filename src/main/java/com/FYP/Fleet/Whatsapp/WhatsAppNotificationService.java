package com.FYP.Fleet.Whatsapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class WhatsAppNotificationService {
    private final RestClient restClient = RestClient.builder().build();

    @Value("${whatsapp.api.url}")
    private String apiUrl;

    @Value("${whatsapp.api.phone-number-id}")
    private String phoneNumberId;

    @Value("${whatsapp.api.access-token}")
    private String accessToken;

    /**
     * Compiles data contexts and fires a Utility notification message directly via Meta infrastructure.
     */
    public void sendTemplateMessage(String toPhone, String templateName, String langCode, List<String> values) {

        // Assemble the single-file mapping container
        WhatsAppPayload payload = new WhatsAppPayload(toPhone, templateName, langCode, values);

        // Interpolate target Graph endpoint URI matching your explicit configuration setup
        String endpointUrl = String.format("%s/%s/messages", apiUrl, phoneNumberId);

        try {
            String apiResponse = restClient.post()
                    .uri(endpointUrl)
                    .headers(httpHeaders -> {
                        httpHeaders.setBearerAuth(accessToken);
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .body(payload)
                    .retrieve()
                    .body(String.class);

            System.out.println("Meta Cloud API Success Response: " + apiResponse);

        } catch (Exception e) {
            System.err.println("Fatal execution failure contacting Meta API: " + e.getMessage());
            throw e;
        }
    }
}
