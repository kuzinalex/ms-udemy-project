package com.example.testappdiscoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TestAppDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestAppDiscoveryServiceApplication.class, args);
	}

}
