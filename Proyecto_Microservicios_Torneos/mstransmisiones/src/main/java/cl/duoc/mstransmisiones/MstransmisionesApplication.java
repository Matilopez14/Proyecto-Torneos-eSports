package cl.duoc.mstransmisiones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MstransmisionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MstransmisionesApplication.class, args);
	}

}
