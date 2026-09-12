package sg.edu.nusiss.whatsappsalesagent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.context.CustomerContext;
import sg.edu.nusiss.whatsappsalesagent.dto.InventoryResponse;
import sg.edu.nusiss.whatsappsalesagent.service.InventoryService;

@Component
@RequiredArgsConstructor
public class InventoryTools {
	
	private final InventoryService inventoryService;
	private final CustomerContext customerContext;
	
	@Tool(description = """
            Check current product inventory.

	        ALWAYS use this tool when a customer asks whether
	        a product or size is available, in stock, sold out,
	        or asks how many units are available.
	
	        Customers may use shortened product names.
	        For example, "Wendy" may refer to "Wendy Dress".
	
	        Customers may express sizes naturally:
	        small = S
	        medium = M
	        large = L
	        extra large = XL
	
	        Never guess inventory.
            """)
	public InventoryResponse checkInventory(String productName, String size) {
		System.out.println("AI TOOL CALLED: checkInventory");
		
		System.out.println("Product: " + productName + ", Size: " + size);
		customerContext.setInventoryChecked(true);
		return inventoryService.checkInventory(productName, size);
	}

}
