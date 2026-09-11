package sg.edu.nusiss.whatsappsalesagent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nusiss.whatsappsalesagent.context.CustomerContext;
import sg.edu.nusiss.whatsappsalesagent.dto.ChatRequest;
import sg.edu.nusiss.whatsappsalesagent.dto.ChatResponse;
import sg.edu.nusiss.whatsappsalesagent.service.ConversationService;
import sg.edu.nusiss.whatsappsalesagent.service.FaqService;
import sg.edu.nusiss.whatsappsalesagent.tool.InventoryTools;
import sg.edu.nusiss.whatsappsalesagent.tool.SalesTools;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
	
	private final ChatClient chatClient;
	private final FaqService faqService;
	private final InventoryTools inventoryTools;
	private final CustomerContext customerContext;
	private final SalesTools salesTools;
	private final ConversationService conversationService;
	
	public ChatController(ChatClient.Builder chatClientBuilder, 
			FaqService faqService, 
			InventoryTools inventoryTools, 
			CustomerContext customerContext,
			SalesTools salesTools,
			ConversationService conversationService) {
		this.chatClient = chatClientBuilder.build();
		this.faqService = faqService;
		this.inventoryTools = inventoryTools;
		this.customerContext = customerContext;
		this.salesTools = salesTools;
		this.conversationService = conversationService;
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
		String customerName = request.customerName();
		String phoneNumber = request.phoneNumber();
		
		customerContext.setCustomerName(customerName);
		customerContext.setPhoneNumber(phoneNumber);
		customerContext.setCustomerMessage(message);
		
		String conversationHistory = conversationService.getConversationHistory(phoneNumber);
		
		conversationService.addUserMessage(phoneNumber, message);
		
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
								
								SALES LEAD RULES:
								
								When a customer clearly expresses an intention to buy
								a product, asks to purchase it, or asks for a sales
								representative to contact them about purchasing it,
								use the createSalesLead tool.
								
								Do not create a lead merely because someone asks
								whether an item is available.
								
								Do not ask the AI to invent a customer name or phone
								number. Customer identity is supplied securely by
								the application.
								
								Only claim that a sales lead has been created after
								the createSalesLead tool succeeds.
															
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
				                
								""" + faqContext +
								"""
							    CONVERSATION HISTORY:

							    """ + conversationHistory + """

							    Use conversation history to understand references
							    such as "it", "that one", "the dress", "yes",
							    "medium", or other follow-up messages.

							    Do not invent details that do not appear in the
							    conversation history, approved company information,
							    or tool results.
							    """)
								.user(message)
								.tools(inventoryTools, salesTools)
								.call()
								.content();
		conversationService.addAssistantMessage(phoneNumber, aiResponse);
		return new ChatResponse(aiResponse, "FAQ", false);
	}

}
