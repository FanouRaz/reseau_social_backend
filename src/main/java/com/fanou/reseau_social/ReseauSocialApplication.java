package com.fanou.reseau_social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
@Import(StaticResourceConfiguration.class)
public class ReseauSocialApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReseauSocialApplication.class, args);
	}
}
