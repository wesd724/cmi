package com.jkb.cmi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CmiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmiApplication.class, args);
	}

}
