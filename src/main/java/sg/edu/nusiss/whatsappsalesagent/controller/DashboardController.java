package sg.edu.nusiss.whatsappsalesagent.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.dto.DashboardStats;
import sg.edu.nusiss.whatsappsalesagent.entity.Escalation;
import sg.edu.nusiss.whatsappsalesagent.entity.SalesLead;
import sg.edu.nusiss.whatsappsalesagent.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {
	
	private final DashboardService dashboardService;
	
	@GetMapping("/stats")
	public DashboardStats getStats() {
		return dashboardService.getStats();
	}
	
	@GetMapping("/leads")
	public List<SalesLead> getLeads() {
		return dashboardService.getSalesLeads();
	}
	
	@GetMapping("/escalations")
	public List<Escalation> getEscalation() {
		return dashboardService.getEscalation();
	}
	
	@PutMapping("/leads/{id}/contacted")
	public SalesLead markLeadAsContacted(@PathVariable Long id) {
		return dashboardService.markLeadAsContacted(id);
	}
	
	@PutMapping("/escalations/{id}/resolve")
	public Escalation resolveEscalation(@PathVariable Long id) {
		return dashboardService.resolveEscalation(id);
	}

}
