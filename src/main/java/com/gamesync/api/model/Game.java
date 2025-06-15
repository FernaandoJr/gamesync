package com.gamesync.api.model;

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
 */
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
	private Set<String> genres = new HashSet<>();
	private Set<String> tags = new HashSet<>();
	private Set<String> platforms = new HashSet<>();
	private GameStatus status;
	private GameSource source;

	@Field("added_at")
	private Date addedAt;

	/**
	 * Construtor padrão.
	 * Necessário para frameworks como Spring Data MongoDB e Jackson (para
	 * desserialização JSON).
	 */
	public Game() { //
	}

	public String getId() { //
		return id;
	}

	public void setId(String id) { //
		this.id = id;
	}

	public String getName() { //
		return name;
	}

	public void setName(String name) { //
		this.name = name;
	}

	public String getDescription() { //
		return description;
	}

	public void setDescription(String description) { //
		this.description = description;
	}

	public String getDeveloper() { //
		return developer;
	}

	public void setDeveloper(String developer) { //
		this.developer = developer;
	}

	public String getUserId() { //
		return userId;
	}

	public void setUserId(String userId) { //
		this.userId = userId;
	}

	public String getImageUrl() { //
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) { //
		this.imageUrl = imageUrl;
	}

	public Integer getHoursPlayed() { //
		return hoursPlayed;
	}

	public void setHoursPlayed(Integer hoursPlayed) { //
		this.hoursPlayed = hoursPlayed;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) { //
		this.favorite = favorite;
	}

	public Set<String> getGenres() { //
		return genres;
	}

	public void setGenres(Set<String> genres) { //
		this.genres = genres;
	}

	public Set<String> getTags() { //
		return tags;
	}

	public void setTags(Set<String> tags) { //
		this.tags = tags;
	}

	public Set<String> getPlatforms() { //
		return platforms;
	}

	public void setPlatforms(Set<String> platforms) { //
		this.platforms = platforms;
	}

	public GameStatus getStatus() { //
		return status;
	}

	public void setStatus(GameStatus status) { //
		this.status = status;
	}

	public GameSource getSource() { //
		return source;
	}

	public void setSource(GameSource source) { //
		this.source = source;
	}

	public Date getAddedAt() { //
		return addedAt;
	}

	public void setAddedAt(Date addedAt) { //
		this.addedAt = addedAt;
	}

	/**
	 * Sobrescreve o método toString para fornecer uma representação textual do
	 * objeto Game.
	 * Útil para logging e depuração.
	 * 
	 * @return Uma String representando o objeto Game.
	 */
	@Override
	public String toString() { //
		return "Game{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", userId='" + userId + '\'' +
				", status=" + status +
				", source=" + source +
				'}';
	}
}