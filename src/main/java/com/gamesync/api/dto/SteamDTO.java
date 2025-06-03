package com.gamesync.api.dto;

import jakarta.validation.constraints.Size; // Define restrições de tamanho (mínimo/máximo) para Strings.
// Para usar @URL, você precisaria da dependência org.hibernate.validator,
// que geralmente é trazida pelo spring-boot-starter-validation.
// Se for usar, descomente o import:
// import org.hibernate.validator.constraints.URL;

/**
 * Data Transfer Object (DTO) para encapsular detalhes específicos de um jogo da plataforma Steam.
 * Esta classe é tipicamente usada como um objeto aninhado dentro de outros DTOs maiores
 * (como GameCreateDTO ou GameUpdateDTO) quando informações do Steam precisam ser enviadas
 * ou recebidas pela API.
 *
 * <p>As anotações de validação garantem que, se os campos forem fornecidos,
 * eles respeitem os limites de tamanho definidos.</p>
 */
public class SteamDTO {

    /**
     * O ID do aplicativo (jogo) na plataforma Steam.
     * Se fornecido, deve ter no máximo 50 caracteres.
     */
    @Size(max = 50, message = "Steam App ID must be up to 50 characters.")
    private String appId;

    /**
     * A URL da página do jogo na loja Steam.
     * Se fornecida, deve ter no máximo 255 caracteres.
     * Poderia opcionalmente ser validada com @URL se o formato exato da URL for crítico.
     */
    @Size(max = 255, message = "Steam Store URL must be up to 255 characters.")
    // Exemplo de como seria com @URL (requer import e dependência se não for transitiva):
    // @URL(message = "Steam Store URL must be a valid URL.")
    private String storeUrl;

    /**
     * A URL da imagem de cabeçalho (header image) do jogo na Steam.
     * Se fornecida, deve ter no máximo 255 caracteres.
     * Poderia opcionalmente ser validada com @URL.
     */
    @Size(max = 255, message = "Steam Header Image URL must be up to 255 characters.")
    // @URL(message = "Steam Header Image URL must be a valid URL.")
    private String headerImageUrl;

    /**
     * Uma representação do progresso de conquistas (achievements) do jogo na Steam.
     * Por exemplo, "50%", "10/20", etc.
     * Se fornecido, deve ter no máximo 50 caracteres.
     */
    @Size(max = 50, message = "Steam Achievement Completion must be up to 50 characters.")
    private String achievementCompletion;

    /**
     * Construtor padrão.
     * Necessário para frameworks de desserialização JSON como Jackson (usado pelo Spring MVC)
     * para instanciar o objeto antes de popular seus campos.
     */
    public SteamDTO() {
    }

    // --- Getters e Setters ---
    // Métodos padrão para acessar e modificar os campos da classe.
    // As anotações de validação nos getters/setters são geralmente redundantes se já estiverem nos campos
    // para DTOs de entrada, pois a validação do Spring (@Valid) atua nos campos (ou propriedades via getters)
    // no momento da desserialização. No entanto, elas não causam problemas aqui.

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    public String getAchievementCompletion() {
        return achievementCompletion;
    }

    public void setAchievementCompletion(String achievementCompletion) {
        this.achievementCompletion = achievementCompletion;
    }
}