package com.proyectogimnasio.planes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PlanesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlanesApplication.class, args);
	}

}
