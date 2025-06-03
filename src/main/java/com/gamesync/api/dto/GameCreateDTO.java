package com.gamesync.api.dto;

import com.gamesync.api.model.GameSource;
import com.gamesync.api.model.GameStatus;
import jakarta.validation.constraints.NotBlank;     // Garante que o campo não seja nulo e não seja apenas espaços em branco.
import jakarta.validation.constraints.NotNull;      // Garante que o campo não seja nulo.
import jakarta.validation.constraints.PositiveOrZero; // Garante que um número seja zero ou positivo.
import jakarta.validation.constraints.Size;        // Define restrições de tamanho (mínimo/máximo) para Strings, coleções, etc.
import java.util.HashSet; // Implementação de Set que não garante ordem.
import java.util.Set;     // Interface para coleções que não permitem elementos duplicados.

/**
 * Data Transfer Object (DTO) para encapsular os dados necessários para criar um novo jogo.
 * Esta classe é usada como corpo da requisição (request body) nos endpoints da API
 * para criação de jogos e inclui validações para garantir a integridade dos dados recebidos.
 */
public class GameCreateDTO {

    /**
     * Nome do jogo.
     * Obrigatório e com tamanho máximo de 255 caracteres.
     */
    @NotBlank(message = "Game name is required.")
    @Size(max = 255, message = "Game name must be up to 255 characters.")
    private String name;

    /**
     * Descrição do jogo.
     * Opcional, com tamanho máximo de 2000 caracteres.
     */
    @Size(max = 2000, message = "Description must be up to 2000 characters.")
    private String description;

    /**
     * Desenvolvedor do jogo.
     * Obrigatório e com tamanho máximo de 100 caracteres.
     */
    @NotBlank(message = "Developer is required.")
    @Size(max = 100, message = "Developer name must be up to 100 characters.")
    private String developer;

    /**
     * Número de horas jogadas.
     * Opcional, mas se fornecido, deve ser zero ou um número positivo.
     */
    @PositiveOrZero(message = "Hours played must be zero or positive.")
    private Integer hoursPlayed;

    /**
     * Indica se o jogo é marcado como favorito.
     * O valor padrão será 'false' se não especificado.
     */
    private boolean favorite;

    /**
     * Conjunto de gêneros do jogo (ex: "RPG", "Aventura").
     * Obrigatório ter pelo menos um gênero. Cada gênero não pode ser em branco e deve ter no máximo 50 caracteres.
     */
    @NotNull(message = "At least one genre is required.") // A coleção em si não pode ser nula.
    private Set<@NotBlank(message = "Genre cannot be blank") @Size(max = 50, message = "Genre must be up to 50 characters") String> genres = new HashSet<>();

    /**
     * Conjunto de tags associadas ao jogo (ex: "Mundo Aberto", "Multiplayer").
     * Obrigatório ter pelo menos uma tag. Cada tag não pode ser em branco e deve ter no máximo 50 caracteres.
     */
    @NotNull(message = "At least one tag is required.") // A coleção em si não pode ser nula.
    private Set<@NotBlank(message = "Tag cannot be blank") @Size(max = 50, message = "Tag must be up to 50 characters") String> tags = new HashSet<>();

    /**
     * Conjunto de plataformas onde o jogo está disponível ou é jogado (ex: "PC", "PlayStation").
     * Obrigatório ter pelo menos uma plataforma. Cada plataforma não pode ser em branco e deve ter no máximo 50 caracteres.
     */
    @NotNull(message = "At least one platform is required.") // A coleção em si não pode ser nula.
    private Set<@NotBlank(message = "Platform cannot be blank") @Size(max = 50, message = "Platform must be up to 50 characters") String> platforms = new HashSet<>();

    /**
     * Status atual do jogo na biblioteca do usuário (ex: PLAYING, COMPLETED).
     * Obrigatório.
     */
    @NotNull(message = "Game status is required.")
    private GameStatus status;

    /**
     * Origem da informação do jogo (ex: STEAM, MANUAL).
     * Obrigatório e com tamanho máximo de 50 caracteres (embora seja um Enum, a anotação @Size aqui pode ser redundante,
     * mas pode servir como uma documentação da expectativa de tamanho se fosse um campo String).
     */
    @NotNull(message = "Game source is required.")
    // A anotação @Size aqui para um Enum é um pouco incomum, pois o valor será um dos identificadores do Enum.
    // A validação principal é que o valor corresponda a um membro válido do GameSource.
    @Size(max = 50, message = "Game source must be up to 50 characters.")
    private GameSource source;

    /**
     * Detalhes específicos do Steam, se a origem do jogo for STEAM.
     * Este campo espera um objeto do tipo SteamDTO. A definição da classe SteamDTO
     * pode estar aninhada nesta classe (como em versões anteriores) ou definida como uma classe separada no mesmo pacote DTO.
     */
    private SteamDTO steam; // Objeto SteamDTO para detalhes do Steam.

    /**
     * Construtor padrão.
     * Necessário para frameworks de desserialização JSON como Jackson (usado pelo Spring MVC)
     * para instanciar o objeto antes de popular seus campos.
     */
    public GameCreateDTO() {
    }

    // --- Getters e Setters ---
    // Métodos para acessar e modificar os campos da classe.
    // As anotações de validação nos getters/setters são geralmente redundantes se já estiverem nos campos
    // para DTOs de entrada, pois a validação do Spring (@Valid) atua nos campos ou propriedades (via getters)
    // no momento da desserialização do corpo da requisição.

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

    public SteamDTO getSteam() {
        return steam;
    }

    public void setSteam(SteamDTO steam) {
        this.steam = steam;
    }

    // A definição da classe aninhada SteamDTO não foi incluída neste trecho de código,
    // mas foi definida em uma interação anterior. Se ela for usada, precisa estar
    // definida aqui como 'public static class SteamDTO { ... }' ou como uma classe separada.
    // Se SteamDTO for uma classe separada, certifique-se de importá-la.
    // Exemplo (se fosse aninhada):
    /*
    public static class SteamDTO {
        @Size(max = 50, message = "Steam App ID must be up to 50 characters.")
        private String appId;
        // ... outros campos, getters e setters ...
    }
    */
}