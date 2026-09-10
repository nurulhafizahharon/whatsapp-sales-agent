package sg.edu.nusiss.whatsappsalesagent.dto;

import java.math.BigDecimal;

public record InventoryResponse(String product, String size, BigDecimal price, int quantity, boolean available, boolean found) {
	
}
