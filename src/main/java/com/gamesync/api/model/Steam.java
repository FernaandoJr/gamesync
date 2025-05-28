// src/main/java/com/gamesync/api/model/SteamDetails.java
package com.gamesync.api.model;

import org.springframework.data.mongodb.core.mapping.Field;

public class Steam {
    @Field("app_id")
    private String appId; // Geralmente um número, mas pode ser tratado como String para flexibilidade
    private String storeUrl;
    private String headerImageUrl;
    private String achievementCompletion;

    // --- Construtores ---
    public Steam() {
    }


    public Steam(String appId, String storeUrl, String headerImageUrl, Double price, String achievementCompletion) {
        this.appId = appId;
        this.storeUrl = storeUrl;
        this.headerImageUrl = headerImageUrl;
        this.achievementCompletion = achievementCompletion;
    }

    // --- Getters e Setters ---

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

    // --- Método toString para facilitar a depuração ---

    @Override
    public String toString() {
        return "SteamDetails{" +
                "appId='" + appId + '\'' +
                ", storeUrl='" + storeUrl + '\'' +
                ", headerImageUrl='" + headerImageUrl + '\'' +
                ", achievementCompletion='" + achievementCompletion + '\'' +
                '}';
    }
}