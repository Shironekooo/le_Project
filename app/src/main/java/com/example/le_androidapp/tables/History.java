package com.example.le_androidapp.tables;

public class History {

    public int historyId;
    public int totalEvent;
    public long totalTime;
    public String usedMode;
    private String userId;

    public int getHistoryId() {
        return historyId;
    }

    public int getTotalEvent() {
        return totalEvent;
    }

    public long getTotalTime() {
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

    public History(int historyId, int totalEvent, long totalTime, String usedMode, String userId) {
        this.historyId = historyId;
        this.totalEvent = totalEvent;
        this.totalTime = totalTime;
        this.usedMode = usedMode;
        this.userId = userId;
    }
}
