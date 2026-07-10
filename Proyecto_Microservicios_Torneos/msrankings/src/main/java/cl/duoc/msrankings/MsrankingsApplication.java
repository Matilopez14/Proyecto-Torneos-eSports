package cl.duoc.msrankings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsrankingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsrankingsApplication.class, args);
	}

}
