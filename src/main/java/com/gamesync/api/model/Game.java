package com.gamesync.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;     // Para representar datas, como a data de adição do jogo.
import java.util.HashSet;  // Implementação de Set para coleções como gêneros, tags e plataformas.
import java.util.Set;      // Interface para coleções que não permitem elementos duplicados.

/**
 * Entidade que representa um jogo no sistema GameSync.
 * Esta classe é mapeada para a coleção "games" no banco de dados MongoDB.
 * Contém informações detalhadas sobre cada jogo, como nome, desenvolvedor,
 * status, horas jogadas, e associação com um usuário.
 */
@Document(collection = "games") // Anotação do Spring Data MongoDB que especifica o nome da coleção no banco.
public class Game {
    @Id // Marca este campo como o identificador único do documento no MongoDB.
    private String id; // O ID único do jogo, gerado pelo MongoDB ou atribuído.
    private String name; // Nome do jogo.
    private String description; // Descrição textual do jogo.
    private String developer; // Nome do desenvolvedor do jogo.

    private String userId; // ID do usuário ao qual este jogo pertence. Usado para associar jogos a usuários.

    @Field("hours_played") // Mapeia este campo Java para o campo "hours_played" no documento MongoDB.
    private Integer hoursPlayed; // Número de horas jogadas. Integer permite valor nulo.
    private boolean favorite; // Indica se o jogo é marcado como favorito pelo usuário.
    private Set<String> genres = new HashSet<>(); // Coleção de gêneros do jogo (ex: RPG, Ação). Inicializado para evitar NullPointerException.
    private Set<String> tags = new HashSet<>(); // Coleção de tags associadas ao jogo (ex: Mundo Aberto, Multiplayer).
    private Set<String> platforms = new HashSet<>(); // Coleção de plataformas em que o jogo está disponível ou é jogado (ex: PC, PlayStation).
    private GameStatus status; // Status atual do jogo na biblioteca do usuário (ex: PLAYING, COMPLETED).
    private GameSource source; // Origem da informação do jogo (ex: STEAM, MANUAL).

    @Field("added_at") // Mapeia este campo Java para o campo "added_at" no documento MongoDB.
    private Date addedAt; // Data e hora em que o jogo foi adicionado à biblioteca do usuário.

    private Steam steam; // Objeto embutido contendo detalhes específicos do Steam, se a origem for STEAM.

    /**
     * Construtor padrão.
     * Necessário para frameworks como Spring Data MongoDB e Jackson (para desserialização JSON).
     */
    public Game() { //
    }

    // --- Getters e Setters ---
    // Métodos padrão para acessar e modificar os campos da classe.

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

    public Integer getHoursPlayed() { //
        return hoursPlayed;
    }

    public void setHoursPlayed(Integer hoursPlayed) { //
        this.hoursPlayed = hoursPlayed;
    }

    public boolean isFavorite() { // Convenção de getter para boolean: "is" + NomeDoCampo.
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

    public Steam getSteam() { //
        return steam;
    }

    public void setSteam(Steam steam) { //
        this.steam = steam;
    }

    /**
     * Sobrescreve o método toString para fornecer uma representação textual do objeto Game.
     * Útil para logging e depuração.
     * @return Uma String representando o objeto Game.
     */
    @Override
    public String toString() { //
        // A representação em String pode ser expandida para incluir mais campos relevantes.
        return "Game{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", source=" + source +
                '}';
    }
}