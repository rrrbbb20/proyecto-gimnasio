package com.example.ms_encargado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class MsEncargadoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsEncargadoApplication.class, args);
	}

}
