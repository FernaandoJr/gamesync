package com.gamesync.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String developer;
    private Integer hoursPlayed;
    private boolean favorite;


    @ElementCollection
    private List<String> genres;

    @ElementCollection
    private List<String> tags;

    @ElementCollection
    private List<String> platforms;

    private OffsetDateTime addedAt;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Enumerated(EnumType.STRING)
    private GameSource source;

    @Embedded
    private Steam steam;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public OffsetDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(OffsetDateTime addedAt) {
        this.addedAt = addedAt;
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

    public Steam getSteam() {
        return steam;
    }

    public void setSteam(Steam steam) {
        this.steam = steam;
    }
}
