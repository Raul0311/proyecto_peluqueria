package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import reactor.core.publisher.Hooks;

/**
 * The Class BackEndApplication.
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BackEndApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();
		SpringApplication app = new SpringApplication(BackEndApplication.class);
        // FORZAMOS EL MODO REACTIVO AQUÍ
        app.setWebApplicationType(WebApplicationType.REACTIVE); 
        app.run(args);
	}

}
