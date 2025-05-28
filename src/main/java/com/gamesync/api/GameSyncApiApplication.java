package com.gamesync.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

// IMPORTANTE: Importe a classe Dotenv
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(
		exclude = {
				JpaRepositoriesAutoConfiguration.class,
				HibernateJpaAutoConfiguration.class
		}
)
public class GameSyncApiApplication {

	public static void main(String[] args) {
		// --- ADICIONE ESTE BLOCO PARA CARREGAR O .env ---
		// Ele vai procurar por um arquivo .env no diretório raiz do projeto
		// e carregar as variáveis para as propriedades do sistema.
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		// --- FIM DO BLOCO .env ---

		SpringApplication.run(GameSyncApiApplication.class, args);
	}

}