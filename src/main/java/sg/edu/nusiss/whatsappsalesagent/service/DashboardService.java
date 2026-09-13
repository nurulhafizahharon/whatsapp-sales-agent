package sg.edu.nusiss.whatsappsalesagent.service;


import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.dto.DashboardStats;
import sg.edu.nusiss.whatsappsalesagent.entity.Escalation;
import sg.edu.nusiss.whatsappsalesagent.entity.SalesLead;
import sg.edu.nusiss.whatsappsalesagent.repository.EscalationRepository;
import sg.edu.nusiss.whatsappsalesagent.repository.ProductRepository;
import sg.edu.nusiss.whatsappsalesagent.repository.SalesLeadRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {
	
	private final SalesLeadRepository salesLeadRepository;
	private final EscalationRepository escalationRepository;
	private final ProductRepository productRepository;
	
	private final SalesLeadService salesLeadService;
	private final EscalationService escalationService;
	
	public DashboardStats getStats() {
		return new DashboardStats(
				salesLeadRepository.countByStatusIgnoreCase("NEW"),
				escalationRepository.countByStatusIgnoreCase("OPEN"),
				productRepository.count());
	}
	
	public List<SalesLead> getSalesLeads() {
		return salesLeadRepository.findAll(Sort.by(Sort.Direction.DESC, SalesLead::getCreatedAt));
	}
	
	public List<Escalation> getEscalation() {
		return escalationRepository.findAll(Sort.by(Sort.Direction.DESC, Escalation::getCreatedAt));
	}
	
	public SalesLead markLeadAsContacted(Long id) {
		return salesLeadService.markAsContacted(id);
	}
	
	public Escalation resolveEscalation(Long id) {
		return escalationService.resolve(id);
	}
	
	

}
