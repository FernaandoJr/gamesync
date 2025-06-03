package com.gamesync.api.model;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Classe que representa detalhes específicos de um jogo proveniente da plataforma Steam.
 * Esta classe é tipicamente embutida como um subdocumento dentro da entidade principal 'Game'
 * quando a origem do jogo é 'STEAM'.
 */
public class Steam { //

    /**
     * O ID do aplicativo (jogo) na plataforma Steam.
     * A anotação @Field especifica que no documento MongoDB, este campo será armazenado como "app_id".
     */
    @Field("app_id") // Mapeia o campo Java 'appId' para o campo 'app_id' no MongoDB.
    private String appId;

    /**
     * A URL da página do jogo na loja Steam.
     */
    private String storeUrl; //

    /**
     * A URL da imagem de cabeçalho (header image) do jogo na Steam.
     */
    private String headerImageUrl; //

    /**
     * Uma representação do progresso de conquistas do jogo na Steam.
     * (Ex: "50%", "10/20 achievements"). O formato exato pode depender da fonte dos dados.
     */
    private String achievementCompletion; //

    /**
     * Construtor padrão.
     * Necessário para frameworks como Spring Data MongoDB e Jackson (para desserialização).
     */
    public Steam() { //
    }

    /**
     * Construtor com todos os campos para facilitar a criação de instâncias da classe.
     * @param appId O ID do aplicativo Steam.
     * @param storeUrl A URL da loja Steam.
     * @param headerImageUrl A URL da imagem de cabeçalho.
     * @param achievementCompletion O status de completude das conquistas.
     */
    public Steam(String appId, String storeUrl, String headerImageUrl, String achievementCompletion) { //
        this.appId = appId;
        this.storeUrl = storeUrl;
        this.headerImageUrl = headerImageUrl;
        this.achievementCompletion = achievementCompletion;
    }

    // --- Getters e Setters ---
    // Métodos padrão para acessar e modificar os campos da classe.

    public String getAppId() { //
        return appId;
    }

    public void setAppId(String appId) { //
        this.appId = appId;
    }

    public String getStoreUrl() { //
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) { //
        this.storeUrl = storeUrl;
    }

    public String getHeaderImageUrl() { //
        return headerImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) { //
        this.headerImageUrl = headerImageUrl;
    }

    public String getAchievementCompletion() { //
        return achievementCompletion;
    }

    public void setAchievementCompletion(String achievementCompletion) { //
        this.achievementCompletion = achievementCompletion;
    }

    /**
     * Sobrescreve o método toString para fornecer uma representação textual do objeto Steam.
     * Útil para logging e depuração.
     * @return Uma String representando o objeto Steam.
     */
    @Override
    public String toString() { //
        return "Steam{" +
                "appId='" + appId + '\'' +
                ", storeUrl='" + storeUrl + '\'' +
                ", headerImageUrl='" + headerImageUrl + '\'' +
                ", achievementCompletion='" + achievementCompletion + '\'' +
                '}';
    }
}