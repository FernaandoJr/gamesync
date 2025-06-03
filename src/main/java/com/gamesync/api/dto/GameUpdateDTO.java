// File: src/main/java/com/gamesync/api/dto/GameUpdateDTO.java
package com.gamesync.api.dto;
import com.gamesync.api.model.GameStatus;
import jakarta.validation.constraints.PositiveOrZero; // Garante que um número seja zero ou positivo.
import jakarta.validation.constraints.Size;        // Define restrições de tamanho (mínimo/máximo).
import java.util.Set;     // Interface para coleções que não permitem elementos duplicados.

/**
 * Data Transfer Object (DTO) para encapsular os dados que podem ser atualizados em um jogo existente.
 * Esta classe é usada como corpo da requisição (request body) nos endpoints da API
 * para atualização de jogos.
 *
 * <p>Diferente do DTO de criação, os campos aqui são geralmente opcionais, permitindo
 * que o cliente envie apenas os dados que deseja modificar. A lógica de serviço
 * tratará quais campos foram fornecidos e aplicará as atualizações correspondentes.</p>
 *
 * <p>As anotações de validação aplicadas aos campos garantem que, se um valor for fornecido,
 * ele deve atender aos critérios especificados (ex: tamanho máximo).</p>
 */
public class GameUpdateDTO {

    /**
     * Novo nome para o jogo.
     * Se fornecido, deve ter no máximo 255 caracteres.
     */
    @Size(max = 255, message = "Game name must be up to 255 characters.")
    private String name;

    /**
     * Nova descrição para o jogo.
     * Se fornecida, deve ter no máximo 2000 caracteres.
     */
    @Size(max = 2000, message = "Description must be up to 2000 characters.")
    private String description;

    /**
     * Novo nome do desenvolvedor do jogo.
     * Se fornecido, deve ter no máximo 100 caracteres.
     */
    @Size(max = 100, message = "Developer name must be up to 100 characters.")
    private String developer;

    /**
     * Novo número de horas jogadas.
     * Se fornecido, deve ser zero ou um número positivo.
     */
    @PositiveOrZero(message = "Hours played must be zero or positive.")
    private Integer hoursPlayed;

    /**
     * Novo status de favorito para o jogo.
     * Usa {@link Boolean} (objeto) para permitir que o valor seja nulo,
     * indicando que o status de favorito não deve ser alterado. Se fornecido (true ou false),
     * o status será atualizado.
     */
    private Boolean favorite;

    /**
     * Novo conjunto de gêneros para o jogo.
     * Se fornecido, substituirá o conjunto existente. Cada gênero na lista
     * deve ter no máximo 50 caracteres.
     */
    private Set<@Size(max = 50) String> genres;

    /**
     * Novo conjunto de tags para o jogo.
     * Se fornecido, substituirá o conjunto existente. Cada tag na lista
     * deve ter no máximo 50 caracteres.
     */
    private Set<@Size(max = 50) String> tags;

    /**
     * Novo conjunto de plataformas para o jogo.
     * Se fornecido, substituirá o conjunto existente. Cada plataforma na lista
     * deve ter no máximo 50 caracteres.
     */
    private Set<@Size(max = 50) String> platforms;

    /**
     * Novo status para o jogo (ex: PLAYING, COMPLETED).
     * Se fornecido, o status será atualizado.
     */
    private GameStatus status;

    /**
     * Novos detalhes do Steam para o jogo.
     * Se fornecido, os detalhes do Steam associados ao jogo serão atualizados.
     * A classe {@code SteamDTO} deve ser definida (geralmente como uma classe separada
     * no mesmo pacote DTO ou como uma classe aninhada em {@code GameCreateDTO} e referenciada aqui).
     */
    private SteamDTO steam; // Referencia um SteamDTO para os detalhes do Steam.

    // Construtor padrão é implicitamente fornecido pelo compilador se nenhum outro construtor for definido.
    // É útil para frameworks de desserialização.

    // --- Getters e Setters ---
    // Métodos padrão para acessar e modificar os campos da classe.
    // Estes são usados pelo framework (ex: Jackson para desserialização JSON) e pelo código da aplicação.

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDeveloper() { return developer; }
    public void setDeveloper(String developer) { this.developer = developer; }

    public Integer getHoursPlayed() { return hoursPlayed; }
    public void setHoursPlayed(Integer hoursPlayed) { this.hoursPlayed = hoursPlayed; }

    public Boolean getFavorite() { return favorite; }
    public void setFavorite(Boolean favorite) { this.favorite = favorite; }

    public Set<String> getGenres() { return genres; }
    public void setGenres(Set<String> genres) { this.genres = genres; }

    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }

    public Set<String> getPlatforms() { return platforms; }
    public void setPlatforms(Set<String> platforms) { this.platforms = platforms; }

    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status = status; }

    public SteamDTO getSteam() { return steam; }
    public void setSteam(SteamDTO steam) { this.steam = steam; }

    // A definição da classe SteamDTO não está incluída neste arquivo.
    // Ela deve existir em algum lugar acessível, por exemplo:
    // public static class SteamDTO { /* campos e getters/setters */ }
    // ou como uma classe separada: com.gamesync.api.dto.SteamDTO
}