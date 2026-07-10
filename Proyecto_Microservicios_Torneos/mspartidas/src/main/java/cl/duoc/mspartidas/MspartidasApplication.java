package cl.duoc.mspartidas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MspartidasApplication {
	public static void main(String[] args) {
		SpringApplication.run(MspartidasApplication.class, args);
	}
}
