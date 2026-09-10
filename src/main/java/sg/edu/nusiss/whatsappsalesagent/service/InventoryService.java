package sg.edu.nusiss.whatsappsalesagent.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.dto.InventoryResponse;
import sg.edu.nusiss.whatsappsalesagent.entity.Inventory;
import sg.edu.nusiss.whatsappsalesagent.repository.InventoryRepository;

@Service
@RequiredArgsConstructor
public class InventoryService {
	
	private final InventoryRepository inventoryRepository;
	
	private String normalizeSize(String size) {
		if(size == null) {
			return null;
		}
		return switch(size.trim().toLowerCase()) {
		case "small", "s" -> "S";
		case "medium", "m" -> "M";
		case "large", "l" -> "L";
		case "extra large","extra-large", "xl" -> "XL";
		default -> size.trim().toUpperCase();
		};
	}
	
	public InventoryResponse checkInventory(String productName, String size) {
		
		String normalizedSize = normalizeSize(size);
		Inventory inventory = inventoryRepository
				.findByProductNameContainingIgnoreCaseAndSizeIgnoreCase(productName, normalizedSize)
				.orElse(null);
		
		if(inventory == null) {
			return new InventoryResponse(productName, size, null, 0, false, false);
		}
		
		return new InventoryResponse(inventory.getProduct().getName(), 
				inventory.getSize(), inventory.getProduct().getPrice(), 
				inventory.getQuantity(), inventory.getQuantity() > 0, true);
	}

}
