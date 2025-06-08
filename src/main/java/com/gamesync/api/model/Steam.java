package com.gamesync.api.model;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Classe que representa detalhes específicos de um jogo proveniente da plataforma Steam.
 * Esta classe é tipicamente embutida como um subdocumento dentro da entidade principal 'Game'
 * quando a origem do jogo é 'STEAM'.
 */
public class Steam { //

    @Field("app_id")
    private String appId;
    private String storeUrl; //
    private String headerImageUrl; //
    private String achievementCompletion; //
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
    public String toString() {
        return "Steam{" +
                "appId='" + appId + '\'' +
                ", storeUrl='" + storeUrl + '\'' +
                ", headerImageUrl='" + headerImageUrl + '\'' +
                ", achievementCompletion='" + achievementCompletion + '\'' +
                '}';
    }
}