package sg.edu.nusiss.whatsappsalesagent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.context.CustomerContext;
import sg.edu.nusiss.whatsappsalesagent.dto.ChatRequest;
import sg.edu.nusiss.whatsappsalesagent.dto.ChatResponse;
import sg.edu.nusiss.whatsappsalesagent.service.ConversationService;
import sg.edu.nusiss.whatsappsalesagent.service.FaqService;
import sg.edu.nusiss.whatsappsalesagent.service.SalesAgentService;
import sg.edu.nusiss.whatsappsalesagent.tool.EscalationTools;
import sg.edu.nusiss.whatsappsalesagent.tool.InventoryTools;
import sg.edu.nusiss.whatsappsalesagent.tool.SalesTools;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
	
	private final SalesAgentService salesAgentService;
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello! WhatsApp Sales Agent is running!";
	}
	
	@PostMapping
	public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
		return salesAgentService.processMessage(
				request.customerName(), 
				request.phoneNumber(), 
				request.message());
				
		
	}
	

}
