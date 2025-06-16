package com.gamesync.api.dto;

import com.gamesync.api.model.GameSource;
import com.gamesync.api.model.GameStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object (DTO) para encapsular os dados necessários para criar um
 * novo jogo.
 * Esta classe é usada como corpo da requisição (request body) nos endpoints da
 * API
 * para criação de jogos e inclui validações para garantir a integridade dos
 * dados recebidos.
 */
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
	private Set<@NotBlank(message = "Genre cannot be blank") @Size(max = 50, message = "Genre must be up to 50 characters") String> genres = new HashSet<>();

	@NotNull(message = "At least one tag is required.") // A coleção em si não pode ser nula.
	private Set<@NotBlank(message = "Tag cannot be blank") @Size(max = 50, message = "Tag must be up to 50 characters") String> tags = new HashSet<>();

	@NotNull(message = "At least one platform is required.") // A coleção em si não pode ser nula.
	private Set<@NotBlank(message = "Platform cannot be blank") @Size(max = 50, message = "Platform must be up to 50 characters") String> platforms = new HashSet<>();

	@NotNull(message = "Game status is required.")
	private GameStatus status;
	@NotNull(message = "Game source is required.")
	@Size(max = 50, message = "Game source must be up to 50 characters.")
	private GameSource source;

	public GameCreateDTO() {
	}

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

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
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

	public GameSource getSource() {
		return source;
	}

	public void setSource(GameSource source) {
		this.source = source;
	}
}