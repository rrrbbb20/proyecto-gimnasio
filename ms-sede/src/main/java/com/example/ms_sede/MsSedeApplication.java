package com.example.ms_sede;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class MsSedeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSedeApplication.class, args);
	}

}
