package com.gamesync.api.dto;
import jakarta.validation.constraints.Size;

public class SteamDTO {
    @Size(max = 50, message = "Steam App ID must be up to 50 characters.")
    private String appId;

    @Size(max = 255, message = "Steam Store URL must be up to 255 characters.")
    // Você pode adicionar @URL aqui se quiser validar o formato da URL,
    // mas precisaria da dependência org.hibernate.validator (que spring-boot-starter-validation já traz)
    // import org.hibernate.validator.constraints.URL;
    private String storeUrl;

    @Size(max = 255, message = "Steam Header Image URL must be up to 255 characters.")
    // @URL
    private String headerImageUrl;

    @Size(max = 50, message = "Steam Achievement Completion must be up to 50 characters.")
    private String achievementCompletion;

    // Construtor padrão para SteamDTO
    public SteamDTO() {
    }

    // Getters e Setters para SteamDTO

    public @Size(max = 50, message = "Steam App ID must be up to 50 characters.") String getAppId() {
        return appId;
    }

    public void setAppId(@Size(max = 50, message = "Steam App ID must be up to 50 characters.") String appId) {
        this.appId = appId;
    }

    public @Size(max = 255, message = "Steam Store URL must be up to 255 characters.") String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(@Size(max = 255, message = "Steam Store URL must be up to 255 characters.") String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public @Size(max = 255, message = "Steam Header Image URL must be up to 255 characters.") String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public void setHeaderImageUrl(@Size(max = 255, message = "Steam Header Image URL must be up to 255 characters.") String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    public @Size(max = 50, message = "Steam Achievement Completion must be up to 50 characters.") String getAchievementCompletion() {
        return achievementCompletion;
    }

    public void setAchievementCompletion(@Size(max = 50, message = "Steam Achievement Completion must be up to 50 characters.") String achievementCompletion) {
        this.achievementCompletion = achievementCompletion;
    }
}