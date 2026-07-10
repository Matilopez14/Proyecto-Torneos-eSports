package cl.duoc.msinscripciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsinscripcionesApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsinscripcionesApplication.class, args);
	}
}
