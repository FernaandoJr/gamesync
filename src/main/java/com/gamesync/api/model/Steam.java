package com.gamesync.api.model;

import java.time.OffsetDateTime;

public class Steam {
    private Long appId;
    private String coverUrl;
    private Double achievementCompletion;
    private OffsetDateTime lastPlayedAt;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Double getAchievementCompletion() {
        return achievementCompletion;
    }

    public void setAchievementCompletion(Double achievementCompletion) {
        this.achievementCompletion = achievementCompletion;
    }

    public OffsetDateTime getLastPlayedAt() {
        return lastPlayedAt;
    }

    public void setLastPlayedAt(OffsetDateTime lastPlayedAt) {
        this.lastPlayedAt = lastPlayedAt;
    }
}
