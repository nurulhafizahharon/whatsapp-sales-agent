package sg.edu.nusiss.whatsappsalesagent.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.entity.Escalation;
import sg.edu.nusiss.whatsappsalesagent.exception.ResourceNotFoundException;
import sg.edu.nusiss.whatsappsalesagent.repository.EscalationRepository;

@Service
@RequiredArgsConstructor
public class EscalationService {
	
	private final EscalationRepository escalationRepository;
	
	public Escalation createEscalation(String customerName, String phoneNumber, String reason, String priority, String customerMessage) {
		Escalation escalation = Escalation.builder()
									.customerName(customerName)
									.phoneNumber(phoneNumber)
									.reason(reason)
									.priority(priority)
									.customerMessage(customerMessage)
									.status("OPEN")
									.createdAt(LocalDateTime.now())
									.build();
		
		return escalationRepository.save(escalation);
	}
	
	public Escalation resolve(Long id) {
		Escalation escalation = escalationRepository
									.findById(id)
									.orElseThrow(() -> 
											new ResourceNotFoundException("Escalation not found: " + id));
		escalation.setStatus("RESOLVED");
		
		return escalationRepository.save(escalation);
	}

}
