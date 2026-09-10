package sg.edu.nusiss.whatsappsalesagent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nusiss.whatsappsalesagent.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	Optional<Product> findByNameIgnoreCase(String name);
	
}
