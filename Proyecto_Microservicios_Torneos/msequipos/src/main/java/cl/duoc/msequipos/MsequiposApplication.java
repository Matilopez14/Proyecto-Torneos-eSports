package cl.duoc.msequipos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsequiposApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsequiposApplication.class, args);
	}
}