package com.gamesync.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

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
		name = "basicAuth", // Um nome para este esquema de segurança
		type = SecuritySchemeType.HTTP, // Tipo de esquema: HTTP
		scheme = "basic" // Esquema HTTP específico: basic
)
public class GameSyncApiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(GameSyncApiApplication.class, args);
	}

}