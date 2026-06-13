package com.example.ms_clase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsClaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsClaseApplication.class, args);
	}

}
