package sg.edu.nusiss.whatsappsalesagent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nusiss.whatsappsalesagent.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{

	Optional<Inventory> findByProductNameContainingIgnoreCaseAndSizeIgnoreCase(String productName, String size);
}
