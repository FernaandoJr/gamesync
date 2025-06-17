package com.gamesync.api.dto;

import com.gamesync.api.model.GameSource;
import com.gamesync.api.model.GameStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object (DTO) para encapsular os dados necessários para criar um
 * novo jogo.
 * Esta classe é usada como corpo da requisição (request body) nos endpoints da
 * API para criação de jogos e inclui validações para garantir a integridade dos
 * dados recebidos.
 * 
 * Usando Lombok para reduzir código boilerplate.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameCreateDTO {

	@NotBlank(message = "Game name is required.")
	@Size(max = 255, message = "Game name must be up to 255 characters.")
	private String name;

	@Size(max = 2000, message = "Description must be up to 2000 characters.")
	private String description;

	@NotBlank(message = "Developer is required.")
	@Size(max = 100, message = "Developer name must be up to 100 characters.")
	private String developer;

	@Size(max = 1024, message = "Image URL must be up to 1024 characters.")
	private String imageUrl;

	@PositiveOrZero(message = "Hours played must be zero or positive.")
	private Integer hoursPlayed;

	private boolean favorite;

	@NotNull(message = "At least one genre is required.")
	@Builder.Default
	private Set<@NotBlank(message = "Genre cannot be blank") @Size(max = 50, message = "Genre must be up to 50 characters") String> genres = new HashSet<>();

	@NotNull(message = "At least one tag is required.") // A coleção em si não pode ser nula.
	@Builder.Default
	private Set<@NotBlank(message = "Tag cannot be blank") @Size(max = 50, message = "Tag must be up to 50 characters") String> tags = new HashSet<>();

	@NotNull(message = "At least one platform is required.") // A coleção em si não pode ser nula.
	@Builder.Default
	private Set<@NotBlank(message = "Platform cannot be blank") @Size(max = 50, message = "Platform must be up to 50 characters") String> platforms = new HashSet<>();

	@NotNull(message = "Game status is required.")
	private GameStatus status;

	@NotNull(message = "Game source is required.")
	private GameSource source;
}