package sg.edu.nusiss.whatsappsalesagent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sg.edu.nusiss.whatsappsalesagent.dto.InventoryResponse;
import sg.edu.nusiss.whatsappsalesagent.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	@GetMapping
	public InventoryResponse checkInventory(@RequestParam String product, @RequestParam String size) {
		return inventoryService.checkInventory(product, size);
	}

}
