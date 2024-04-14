package com.webservice.MatchCraft.model;

public class OnlineStatusMessage {
    private Integer userId;
    private boolean isOnline;

    public OnlineStatusMessage(Integer userId, boolean isOnline) {
        this.userId = userId;
        this.isOnline = isOnline;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
