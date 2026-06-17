package com.proyectogimnasio.rutina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RutinaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RutinaApplication.class, args);
	}

}
