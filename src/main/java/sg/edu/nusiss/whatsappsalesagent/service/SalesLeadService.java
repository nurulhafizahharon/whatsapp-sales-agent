package sg.edu.nusiss.whatsappsalesagent.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.entity.SalesLead;
import sg.edu.nusiss.whatsappsalesagent.exception.ResourceNotFoundException;
import sg.edu.nusiss.whatsappsalesagent.repository.SalesLeadRepository;

@Service
@RequiredArgsConstructor
public class SalesLeadService {
	
	private final SalesLeadRepository salesLeadRepository;
	
	public SalesLead createLead(String customerName, String phoneNumber, String productName, String customerMessage) {
		SalesLead lead = SalesLead.builder()
							.customerName(customerName)
							.phoneNumber(phoneNumber)
							.productName(productName)
							.customerMessage(customerMessage)
							.status("NEW")
							.createdAt(LocalDateTime.now())
							.build();
		return salesLeadRepository.save(lead);
	}
	
	public SalesLead markAsContacted(Long id) {
		SalesLead lead = salesLeadRepository
							.findById(id)
							.orElseThrow(() -> 
								new ResourceNotFoundException("Sales lead not found: " + id));
		lead.setStatus("CONTACTED");
		
		return salesLeadRepository.save(lead);
	}

}
