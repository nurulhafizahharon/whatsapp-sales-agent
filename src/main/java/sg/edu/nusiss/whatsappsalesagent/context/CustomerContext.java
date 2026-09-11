package sg.edu.nusiss.whatsappsalesagent.context;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import lombok.Getter;
import lombok.Setter;

@Component
@RequestScope
@Getter
@Setter
public class CustomerContext {
	
	private String customerName;
	private String phoneNumber;
	private String customerMessage;

}
