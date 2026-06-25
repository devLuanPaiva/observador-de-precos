package com.luanpaiva.observador_de_precos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ObservadorDePrecosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObservadorDePrecosApplication.class, args);
	}

}
