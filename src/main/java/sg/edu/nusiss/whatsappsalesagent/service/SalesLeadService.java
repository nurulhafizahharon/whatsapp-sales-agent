package sg.edu.nusiss.whatsappsalesagent.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.entity.SalesLead;
import sg.edu.nusiss.whatsappsalesagent.repository.SalesLeadRepository;

@Service
@RequiredArgsConstructor
public class SalesLeadService {
	
	private final SalesLeadRepository salesRepository;
	
	public SalesLead createLead(String customerName, String phoneNumber, String productName, String customerMessage) {
		SalesLead lead = SalesLead.builder()
							.customerName(customerName)
							.phoneNumber(phoneNumber)
							.productName(productName)
							.customerMessage(customerMessage)
							.status("NEW")
							.createdAt(LocalDateTime.now())
							.build();
		return salesRepository.save(lead);
	}

}
