package sg.edu.nusiss.whatsappsalesagent.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.dto.ChatResponse;
import sg.edu.nusiss.whatsappsalesagent.service.SalesAgentService;
import sg.edu.nusiss.whatsappsalesagent.service.WhatsAppService;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api/whatsapp/webhook")
@RequiredArgsConstructor
public class WhatsAppWebhookController {
	
	private final ObjectMapper objectMapper;
	private final SalesAgentService salesAgentService;
	private final WhatsAppService whatsAppService;
	
	@Value("${whatsapp.verify-token}")
	private String verifyToken;
	
	@GetMapping
	public ResponseEntity<String> verifyWebhook(@RequestParam(name = "hub.mode") String mode, 
									@RequestParam(name = "hub.verify_token") String token, 
									@RequestParam(name = "hub.challenge") String challenge) {
		System.out.println("WhatsApp webhook verification received");
		
		if("subscribe".equals(mode) && verifyToken.equals(token)) {
			System.out.println("WhatsApp webhook verified successfully");
			return ResponseEntity.ok(challenge);
		}
		
		System.out.println("WhatsApp webhook verification failed");
		
		return ResponseEntity.status(403).body("Verification failed");
		
	}
	
	@PostMapping
	public ResponseEntity<Void> receiveWebhook(@RequestBody String payload) {

		try {
			JsonNode root = objectMapper.readTree(payload);

	        JsonNode value = root
	                .path("entry")
	                .path(0)
	                .path("changes")
	                .path(0)
	                .path("value");

	        JsonNode messages = value.path("messages");

	        // Some WhatsApp webhook events are not customer messages.
	        if (!messages.isArray() || messages.isEmpty()) {
	            System.out.println("WhatsApp webhook received - no customer message");
	            return ResponseEntity.ok().build();
	        }

	        JsonNode messageNode = messages.get(0);

	        String messageType =
	                messageNode.path("type").stringValue();

	        // For now our agent only handles text.
	        if (!"text".equals(messageType)) {
	            System.out.println("Ignoring WhatsApp message type: " + messageType);
	            return ResponseEntity.ok().build();
	        }

	        String phoneNumber = messageNode.path("from").stringValue();

	        String message = messageNode
	                        .path("text")
	                        .path("body")
	                        .stringValue();

	        JsonNode nameNode = value
	                .path("contacts")
	                .path(0)
	                .path("profile")
	                .path("name");

	        String customerName =
	                nameNode.isString()
	                        ? nameNode.stringValue()
	                        : "WhatsApp Customer";

	        System.out.println();
	        System.out.println("========== WHATSAPP MESSAGE ==========");
	        System.out.println("Customer: " + customerName);
	        System.out.println("Phone: " + phoneNumber);
	        System.out.println("Message: " + message);
	        System.out.println("======================================");
	        
	        ChatResponse response = salesAgentService.processMessage(customerName, phoneNumber, message);

	        System.out.println();
	        System.out.println("========== AI RESPONSE ==========");
	        System.out.println("Response: " + response.response());
	        System.out.println("Type: " + response.type());
	        System.out.println("Escalated: " + response.escalated());
	        System.out.println("=================================");
	        
	        whatsAppService.sendTextMessage(phoneNumber,response.response());
		} catch (Exception e) {
			System.out.println("Error processing WhatsApp webhook: " + e.getMessage());
		}
		
//	    System.out.println();
//	    System.out.println("========== WHATSAPP WEBHOOK ==========");
//	    System.out.println(payload);
//	    System.out.println("======================================");
//	    System.out.println();

	    return ResponseEntity.ok().build();
	}
	
//	@PostMapping("/send-test")
//	public ResponseEntity<String> sendTest(@RequestParam String to) {
//	    whatsAppService.sendTextMessage(to,"Hello from my Spring Boot WhatsApp Sales Agent!");
//	    return ResponseEntity.ok("Message sent");
//	}
	
}
