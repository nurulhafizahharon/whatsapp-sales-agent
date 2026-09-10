package sg.edu.nusiss.whatsappsalesagent.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sg.edu.nusiss.whatsappsalesagent.entity.Inventory;
import sg.edu.nusiss.whatsappsalesagent.entity.Product;
import sg.edu.nusiss.whatsappsalesagent.repository.InventoryRepository;
import sg.edu.nusiss.whatsappsalesagent.repository.ProductRepository;

@Configuration
public class DemoDataConfig {

	@Bean
	CommandLineRunner loadDemoData(ProductRepository productRepository, 
			InventoryRepository inventoryRepository) {
		
		return args -> {
			if(productRepository.count() > 0) {
				return;
			}
			
			Product wendy = Product.builder()
                    .name("Wendy Dress")
                    .description("Demo product for hackathon")
                    .price(new BigDecimal("76.00"))
                    .build();

            productRepository.save(wendy);

            inventoryRepository.save(
                    Inventory.builder()
                            .product(wendy)
                            .size("S")
                            .quantity(4)
                            .build()
            );

            inventoryRepository.save(
                    Inventory.builder()
                            .product(wendy)
                            .size("M")
                            .quantity(2)
                            .build()
            );

            inventoryRepository.save(
                    Inventory.builder()
                            .product(wendy)
                            .size("L")
                            .quantity(0)
                            .build()
            );

            Product cassandra = Product.builder()
                    .name("Cassandra Dress")
                    .description("Demo product for hackathon")
                    .price(new BigDecimal("72.00"))
                    .build();

            productRepository.save(cassandra);

            inventoryRepository.save(
                    Inventory.builder()
                            .product(cassandra)
                            .size("S")
                            .quantity(3)
                            .build()
            );

            inventoryRepository.save(
                    Inventory.builder()
                            .product(cassandra)
                            .size("M")
                            .quantity(1)
                            .build()
            );

            inventoryRepository.save(
                    Inventory.builder()
                            .product(cassandra)
                            .size("L")
                            .quantity(5)
                            .build()
            );
		};
	}
}
