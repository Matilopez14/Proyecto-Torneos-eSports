package com.example.mspremios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; // Se importará de forma automática

@EnableFeignClients
@SpringBootApplication
public class MspremiosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MspremiosApplication.class, args);
	}
}