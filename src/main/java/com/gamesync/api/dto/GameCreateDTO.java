// File: src/main/java/com/gamesync/api/dto/GameCreateDTO.java
package com.gamesync.api.dto;

import com.gamesync.api.model.GameSource;
import com.gamesync.api.model.GameStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;



public class GameCreateDTO {

    @NotBlank(message = "Game name is required.")
    @Size(max = 255, message = "Game name must be up to 255 characters.")
    private String name;

    @Size(max = 2000, message = "Description must be up to 2000 characters.")
    private String description;

    @NotBlank(message = "Developer is required.")
    @Size(max = 100, message = "Developer name must be up to 100 characters.")
    private String developer;

    @PositiveOrZero(message = "Hours played must be zero or positive.")
    private Integer hoursPlayed;

    private boolean favorite;

    @NotNull(message = "At least one genre is required.")
    private Set<@NotBlank(message = "Genre cannot be blank") @Size(max = 50, message = "Genre must be up to 50 characters") String> genres = new HashSet<>();

    @NotNull(message = "At least one tag is required.")
    private Set<@NotBlank(message = "Tag cannot be blank") @Size(max = 50, message = "Tag must be up to 50 characters") String> tags = new HashSet<>();

    @NotNull(message = "At least one platform is required.")
    private Set<@NotBlank(message = "Platform cannot be blank") @Size(max = 50, message = "Platform must be up to 50 characters") String> platforms = new HashSet<>();

    @NotNull(message = "Game status is required.")
    private GameStatus status;

    @NotNull(message = "Game source is required.")
    @Size(max = 50, message = "Game source must be up to 50 characters.")
    private GameSource source;

    private SteamDTO steam;

    // Construtor padrão (importante para frameworks como Jackson)
    public GameCreateDTO() {
    }

    // Getters e Setters
    public @NotBlank(message = "Game name is required.") @Size(max = 255, message = "Game name must be up to 255 characters.") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Game name is required.") @Size(max = 255, message = "Game name must be up to 255 characters.") String name) {
        this.name = name;
    }

    public @Size(max = 2000, message = "Description must be up to 2000 characters.") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 2000, message = "Description must be up to 2000 characters.") String description) {
        this.description = description;
    }

    public @NotBlank(message = "Developer is required.") @Size(max = 100, message = "Developer name must be up to 100 characters.") String getDeveloper() {
        return developer;
    }

    public void setDeveloper(@NotBlank(message = "Developer is required.") @Size(max = 100, message = "Developer name must be up to 100 characters.") String developer) {
        this.developer = developer;
    }

    public @PositiveOrZero(message = "Hours played must be zero or positive.") Integer getHoursPlayed() {
        return hoursPlayed;
    }

    public void setHoursPlayed(@PositiveOrZero(message = "Hours played must be zero or positive.") Integer hoursPlayed) {
        this.hoursPlayed = hoursPlayed;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public @NotNull(message = "At least one genre is required.") Set<@NotBlank(message = "Genre cannot be blank") @Size(max = 50, message = "Genre must be up to 50 characters") String> getGenres() {
        return genres;
    }

    public void setGenres(@NotNull(message = "At least one genre is required.") Set<@NotBlank(message = "Genre cannot be blank") @Size(max = 50, message = "Genre must be up to 50 characters") String> genres) {
        this.genres = genres;
    }

    public @NotNull(message = "At least one tag is required.") Set<@NotBlank(message = "Tag cannot be blank") @Size(max = 50, message = "Tag must be up to 50 characters") String> getTags() {
        return tags;
    }

    public void setTags(@NotNull(message = "At least one tag is required.") Set<@NotBlank(message = "Tag cannot be blank") @Size(max = 50, message = "Tag must be up to 50 characters") String> tags) {
        this.tags = tags;
    }

    public @NotNull(message = "At least one platform is required.") Set<@NotBlank(message = "Platform cannot be blank") @Size(max = 50, message = "Platform must be up to 50 characters") String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(@NotNull(message = "At least one platform is required.") Set<@NotBlank(message = "Platform cannot be blank") @Size(max = 50, message = "Platform must be up to 50 characters") String> platforms) {
        this.platforms = platforms;
    }

    public @NotNull(message = "Game status is required.") GameStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Game status is required.") GameStatus status) {
        this.status = status;
    }

    public @NotNull(message = "Game source is required.") @Size(max = 50, message = "Game source must be up to 50 characters.") GameSource getSource() {
        return source;
    }

    public void setSource(@NotNull(message = "Game source is required.") @Size(max = 50, message = "Game source must be up to 50 characters.") GameSource source) {
        this.source = source;
    }

    public SteamDTO getSteam() {
        return steam;
    }

    public void setSteam(SteamDTO steam) {
        this.steam = steam;
    }
}