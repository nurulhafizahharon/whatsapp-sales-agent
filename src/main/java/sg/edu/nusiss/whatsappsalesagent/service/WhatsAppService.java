package sg.edu.nusiss.whatsappsalesagent.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WhatsAppService {

	private final RestClient restClient;

    @Value("${whatsapp.access-token}")
    private String accessToken;

    @Value("${whatsapp.phone-number-id}")
    private String phoneNumberId;

    public WhatsAppService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public void sendTextMessage(
            String recipientPhoneNumber,
            String message) {

        String url =
                "https://graph.facebook.com/v26.0/"
                + phoneNumberId
                + "/messages";

        Map<String, Object> body = Map.of(
                "messaging_product", "whatsapp",
                "recipient_type", "individual",
                "to", recipientPhoneNumber,
                "type", "text",
                "text", Map.of(
                        "body", message
                )
        );

        restClient.post()
                .uri(url)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        System.out.println(
                "WhatsApp reply sent to: "
                + recipientPhoneNumber
        );
    }
}
