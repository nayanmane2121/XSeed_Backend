package com.tcognition.recruiterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableFeignClients(basePackages = "com.tcognition.recruiterservice.service")
//@ComponentScan(basePackages = {"com.tcognition.audit"}) 
@EnableJpaAuditing
@EnableDiscoveryClient
public class RecruiterServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RecruiterServiceApplication.class, args);

		}
}
