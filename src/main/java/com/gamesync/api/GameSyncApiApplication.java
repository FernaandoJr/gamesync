package com.gamesync.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication(
		exclude = {
				JpaRepositoriesAutoConfiguration.class,
				HibernateJpaAutoConfiguration.class
		}
)
@OpenAPIDefinition(
		info = @Info(
				title = "GameSync API",
				version = "0.0.1-SNAPSHOT",
				description = "API para gerenciamento de jogos e perfis de usuários do GameSync."

		)
)
@SecurityScheme(
		name = "basicAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "basic"
)
public class GameSyncApiApplication {
	private static final Logger logger = LoggerFactory.getLogger(GameSyncApiApplication.class);

	public static void main(String[] args) {
		// Load .env file and set the environment variables before Spring Boot starts
		Dotenv dotenv = Dotenv.configure().load();

		// Set all environment variables from the .env file as system properties
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		// Run the application
		SpringApplication.run(GameSyncApiApplication.class, args);

		logger.info("Application started successfully with MongoDB configuration loaded from .env file");
	}

}