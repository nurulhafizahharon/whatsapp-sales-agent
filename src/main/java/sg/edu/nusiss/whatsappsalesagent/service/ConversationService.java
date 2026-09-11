package sg.edu.nusiss.whatsappsalesagent.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class ConversationService {
	
	private final Map<String, List<String>> conversations = new ConcurrentHashMap<>();

	public void addUserMessage(String phoneNumber, String message) {
		getConversation(phoneNumber).add("Customer: " + message);
	}
	
	public void addAssistantMessage(String phoneNumber, String message) {
		getConversation(phoneNumber).add("Assistant: " + message);
	}
	
	public String getConversationHistory(String phoneNumber) {
		List<String> history = conversations.get(phoneNumber);
		
		if(history == null || history.isEmpty()) {
			return "No previous conversation.";
		}
		
		return String.join("\n", history);
	}
	
	public List<String> getConversation(String phoneNumber) {
		return conversations.computeIfAbsent(phoneNumber, key -> new ArrayList<>());
	}
}

