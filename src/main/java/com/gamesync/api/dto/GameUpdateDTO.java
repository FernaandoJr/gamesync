// File: src/main/java/com/gamesync/api/dto/GameUpdateDTO.java
package com.gamesync.api.dto;

import com.gamesync.api.model.GameStatus;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * Data Transfer Object (DTO) para encapsular os dados que podem ser atualizados
 * em um jogo existente.
 * Esta classe é usada como corpo da requisição (request body) nos endpoints da
 * API
 * para atualização de jogos.
 *
 * <p>
 * Diferente do DTO de criação, os campos aqui são geralmente opcionais,
 * permitindo
 * que o cliente envie apenas os dados que deseja modificar. A lógica de serviço
 * tratará quais campos foram fornecidos e aplicará as atualizações
 * correspondentes.
 * </p>
 *
 * <p>
 * As anotações de validação aplicadas aos campos garantem que, se um valor for
 * fornecido,
 * ele deve atender aos critérios especificados (ex: tamanho máximo).
 * </p>
 */
public class GameUpdateDTO {

	@Size(max = 255, message = "Game name must be up to 255 characters.")
	private String name;

	@Size(max = 2000, message = "Description must be up to 2000 characters.")
	private String description;

	@Size(max = 100, message = "Developer name must be up to 100 characters.")
	private String developer;

	@Size(max = 1024, message = "Image URL must be up to 1024 characters.")
	private String imageUrl;

	@PositiveOrZero(message = "Hours played must be zero or positive.")
	private Integer hoursPlayed;

	private Boolean favorite;

	private Set<@Size(max = 50) String> genres;

	private Set<@Size(max = 50) String> tags;

	private Set<@Size(max = 50) String> platforms;

	private GameStatus status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getHoursPlayed() {
		return hoursPlayed;
	}

	public void setHoursPlayed(Integer hoursPlayed) {
		this.hoursPlayed = hoursPlayed;
	}

	public Boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}

	public Set<String> getGenres() {
		return genres;
	}

	public void setGenres(Set<String> genres) {
		this.genres = genres;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Set<String> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(Set<String> platforms) {
		this.platforms = platforms;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

}