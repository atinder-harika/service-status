package com.atinder.service_status_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceStatusBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceStatusBackendApplication.class, args);
	}

}
