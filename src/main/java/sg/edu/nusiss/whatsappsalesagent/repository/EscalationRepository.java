package sg.edu.nusiss.whatsappsalesagent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nusiss.whatsappsalesagent.entity.Escalation;

public interface EscalationRepository extends JpaRepository<Escalation, Long> {

	long countByStatusIgnoreCase(String status);
	
}
