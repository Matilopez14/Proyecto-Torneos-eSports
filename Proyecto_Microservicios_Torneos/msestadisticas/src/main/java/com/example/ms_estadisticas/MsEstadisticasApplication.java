package com.example.ms_estadisticas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsEstadisticasApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsEstadisticasApplication.class, args);
	}
}