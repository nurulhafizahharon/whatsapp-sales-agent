package sg.edu.nusiss.whatsappsalesagent.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sales_lead")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesLead {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String customerName;
	
	private String phoneNumber;
	
	private String productName;
	
	private String customerMessage;
	
	private String status;
	
	private LocalDateTime createdAt;

}
