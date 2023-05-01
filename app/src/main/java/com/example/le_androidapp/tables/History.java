package com.example.le_androidapp.tables;

public class History {

    public String historyId;
    public String totalEvent;
    public String totalTime;
    public String usedMode;
    private String userId;

    public String getHistoryId() {
        return historyId;
    }

    public String getTotalEvent() {
        return totalEvent;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getUsedMode() {
        return usedMode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public History(String historyId, String totalEvent, String totalTime, String usedMode, String userId) {
        this.historyId = historyId;
        this.totalEvent = totalEvent;
        this.totalTime = totalTime;
        this.usedMode = usedMode;
        this.userId = userId;
    }
}
