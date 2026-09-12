package sg.edu.nusiss.whatsappsalesagent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.context.CustomerContext;
import sg.edu.nusiss.whatsappsalesagent.entity.Escalation;
import sg.edu.nusiss.whatsappsalesagent.service.EscalationService;

@Component
@RequiredArgsConstructor
public class EscalationTools {
	
	private final EscalationService escalationService;
	private final CustomerContext customerContext;
	
	@Tool(description = """
            Escalate an enquiry to a human sales representative.

            Use this tool when the customer has an issue that cannot
            safely or fully be resolved using approved FAQ information
            or other available tools.

            Examples include:
            - missing or significantly delayed parcels
            - complaints requiring investigation
            - refund disputes
            - payment problems requiring staff intervention
            - repeated unresolved issues
            - explicit requests to speak to a human

            Do not use this tool for ordinary FAQ questions or simple
            inventory checks.

            Provide a concise reason and choose priority:
            LOW, MEDIUM, or HIGH.
            """)
	public String escalationToHuman(String reason, String priority) {
		System.out.println("AI TOOL CALLED: escalateToHuman");
		
		System.out.println("Reason: " + reason);
		System.out.println("Priority: " + priority);
		
		Escalation escalation = escalationService.createEscalation(
										customerContext.getCustomerName(), 
										customerContext.getPhoneNumber(), 
										reason, 
										priority, 
										customerContext.getCustomerMessage());
		customerContext.setEscalated(true);
		return "Escalation created successfully with ID " + escalation.getId();
	}
	

}
