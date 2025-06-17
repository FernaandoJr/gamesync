package com.gamesync.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa um jogo no sistema GameSync.
 * Esta classe é mapeada para a coleção "games" no banco de dados MongoDB.
 * Contém informações detalhadas sobre cada jogo, como nome, desenvolvedor,
 * status, horas jogadas, e associação com um usuário.
 * 
 * Usando Lombok para reduzir código boilerplate:
 * - @Data: gera getters, setters, equals, hashCode e toString
 * - @NoArgsConstructor: gera construtor sem argumentos
 * - @AllArgsConstructor: gera construtor com todos os argumentos
 * - @Builder: implementa o padrão Builder para criação de objetos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "games")
public class Game {
	@Id
	private String id;
	private String name;
	private String description;
	private String developer;
	private String userId;
	private String imageUrl;

		@Field("hours_played")
		private Integer hoursPlayed;
		private boolean favorite;

		@Builder.Default
		private Set<String> genres = new HashSet<>();

		@Builder.Default
		private Set<String> tags = new HashSet<>();

		@Builder.Default
		private Set<String> platforms = new HashSet<>();

		private GameStatus status;
		private GameSource source;

		@Field("added_at")
		private Date addedAt;
}