package com.jkb.cmi;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class CmiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmiApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		System.out.println("ApplicationReadyEvent는 CommandLineRunner와 ApplicationRunner 끝나고 1");
	}
	@EventListener
	public void init(ApplicationReadyEvent event) {
		System.out.println("ApplicationReadyEvent는 CommandLineRunner와 ApplicationRunner 끝나고 2");
	}
	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> System.out.println("CommandLineRunner와 ApplicationRunner는 거의 동시에 실행 1");
	}
	@Bean
	public ApplicationRunner applicationRunner() {
		return args -> {
			System.out.println("CommandLineRunner와 ApplicationRunner는 거의 동시에 실행 2");
		};
	}

	@EventListener
	public void init(ContextRefreshedEvent event) {
		System.out.println("ContextRefreshedEvent");
	}
	@EventListener
	public void init(ContextClosedEvent event) {
		System.out.println("ContextClosedEvent");
	}
}
