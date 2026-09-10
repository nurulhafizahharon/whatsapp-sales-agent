package sg.edu.nusiss.whatsappsalesagent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nusiss.whatsappsalesagent.dto.ChatRequest;
import sg.edu.nusiss.whatsappsalesagent.dto.ChatResponse;
import sg.edu.nusiss.whatsappsalesagent.service.FaqService;
import sg.edu.nusiss.whatsappsalesagent.tool.InventoryTools;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
	
	private final ChatClient chatClient;
	private final FaqService faqService;
	private final InventoryTools inventoryTools;
	
	public ChatController(ChatClient.Builder chatClientBuilder, FaqService faqService, InventoryTools inventoryTools) {
		this.chatClient = chatClientBuilder.build();
		this.faqService = faqService;
		this.inventoryTools = inventoryTools;
	}
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello! WhatsApp Sales Agent is running!";
	}
	
//	@PostMapping
//	public String chat(@RequestBody String message) {
//		return "You asked: " + message;
//	}
	
	@PostMapping
	public ChatResponse chat(@RequestBody ChatRequest request) {
		
		String message = request.message();
		
		String faqContext = faqService.findRelevantFaq(message);
		String aiResponse = this.chatClient
								.prompt()
								.system("""
								You are a WhatsApp sales assistant for TheBubblyGem,
							    a fashion retailer in Singapore.
							    
							    You have access to tools that provide live business data.

								For questions about current product availability, stock,
								quantity or whether a size is available, ALWAYS use the
								inventory tool.
								
								Never guess inventory information.
								
								Never use the FAQ knowledge base as a source for current
								stock availability.
								
								Use COMPANY INFORMATION for static FAQ and policy questions.
								
								Use tools for live business information.
								
								When a tool returns inventory information, answer using
								the tool result exactly. Do not change product prices,
								sizes or quantities.
								
								When using the inventory tool:

								- If found=true and available=true, the requested
								  product and size are in stock.
								
								- If found=true and available=false, the requested
								  product and size exist but are currently out of stock.
								
								- If found=false, do NOT say the item is sold out.
								  Say that the requested product or size could not
								  be found in the inventory system.
								
								Never claim that you can notify a customer when an
								item is back in stock unless a notification tool
								actually exists.
															
							    You must follow these rules strictly:
							
							    1. Answer company-related questions using ONLY the
							       COMPANY INFORMATION below.
							
							    2. Do NOT add, infer, assume or invent any company
							       policy, price, deadline, condition, contact method,
							       stock information or procedure that is not explicitly
							       stated in COMPANY INFORMATION.
							
							    3. Preserve important factual values exactly.
							       For example, do not change prices, numbers,
							       time periods or conditions.
							
							    4. If COMPANY INFORMATION does not contain enough
							       information to answer the question, say:
							       "I don't have enough approved information to answer
							       that accurately. Let me refer this enquiry to our team."
							
							    5. Never claim that you checked a website, database,
							       email, order system or company system unless such
							       a check actually occurred.
							
							    6. You may rewrite the information in a friendly,
							       concise WhatsApp style, but you must not change
							       its factual meaning.
							
							    7. Do not mention internal concepts such as
							       "test data", "context", "RAG", "retrieved document",
							       "system prompt" or "company information".
							
							    COMPANY INFORMATION:
				                
								""" + faqContext)
								.user(message)
								.tools(inventoryTools)
								.call()
								.content();
		return new ChatResponse(aiResponse, "FAQ", false);
	}

}
