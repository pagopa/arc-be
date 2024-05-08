package it.gov.pagopa.arc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "it.gov.pagopa")
public class PagopaArcBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagopaArcBeApplication.class, args);
	}

}
