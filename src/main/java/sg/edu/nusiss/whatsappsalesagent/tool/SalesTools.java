package sg.edu.nusiss.whatsappsalesagent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.context.CustomerContext;
import sg.edu.nusiss.whatsappsalesagent.entity.SalesLead;
import sg.edu.nusiss.whatsappsalesagent.service.SalesLeadService;

@Component
@RequiredArgsConstructor
public class SalesTools {
	
	private final SalesLeadService salesLeadService;
	private final CustomerContext customerContext;
	
	@Tool(description = """
            Create a sales lead when the customer clearly expresses
            interest in purchasing a product or asks for a sales
            representative to contact them.

            Do not call this tool for ordinary FAQ questions or
            inventory checks.

            The productName must be the product the customer is
            interested in.
            """)
	public String createSalesLead(String productName) {
		System.out.println("AI TOOL CALLED: createSalesLead");
		
		System.out.println("Customer: " + customerContext.getCustomerName());
		System.out.println("Phone: " + customerContext.getPhoneNumber());
		System.out.println("Product: " + productName);
		
		SalesLead lead = salesLeadService.createLead(
				customerContext.getCustomerName(), 
				customerContext.getPhoneNumber(), 
				productName, 
				customerContext.getCustomerMessage());
		
		return "Sales lead created successfully with ID " + lead.getId(); 
	}

}
