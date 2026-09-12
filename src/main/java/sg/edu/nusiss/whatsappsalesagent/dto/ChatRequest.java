package sg.edu.nusiss.whatsappsalesagent.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
		@NotBlank(message = "Customer name is required")
		String customerName, 
		
		@NotBlank(message = "Phone number is required")
		String phoneNumber, 
		
		@NotBlank(message = "Message is required")
		String message) {

}
