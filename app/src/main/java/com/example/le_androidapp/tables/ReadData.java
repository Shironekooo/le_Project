package com.example.le_androidapp.tables;

public class ReadData {
    private String dataId;
    private int eventBad;
    private long recordedTime;

    private String userId;

    public ReadData() {}

    public ReadData(String dataId, int eventBad, long recordedTime, String userId) {
        this.dataId = dataId;
        this.eventBad = eventBad;
        this.recordedTime = recordedTime;
        this.userId = userId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public int getEventBad() {
        return eventBad;
    }

    public void setEventBad(int eventBad) {
        this.eventBad = eventBad;
    }

    public long getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(long recordedTime) {
        this.recordedTime = recordedTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

