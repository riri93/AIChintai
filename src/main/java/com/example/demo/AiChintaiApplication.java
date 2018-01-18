package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration.class,
		SecurityAutoConfiguration.class })
@EntityScan(basePackages = { "com.example.entity" })
@EnableJpaRepositories(basePackages = { "com.example.repository" })
@ComponentScan(basePackages = { "com.example.controller" })
@ComponentScan(basePackages = { "com.example.service" })
@ComponentScan(basePackages = { "com.example.tool" })
@ComponentScan(basePackages = { "com.example.web" })
public class AiChintaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiChintaiApplication.class, args);
	}
}
