package com.example.le_androidapp.tables;

public class ReadData {

    public int eventBad;
    public String recordedTime;
    public String userId;
    public int totalEvent;
    public String totalTime;

    public ReadData() {

    }

    public ReadData(String userId) {
        this.userId=userId;
    }


    public int getEventBad() {
        return eventBad;
    }

    public void setTotalEvent(int totalEvent) {
        this.totalEvent = totalEvent;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void setEventBad(int badPostureCount) {
        this.eventBad = badPostureCount;
    }
    public String getRecordedTime() {
        return recordedTime;
    }
    public void setRecordedTime(String recordedTime) {
        this.recordedTime = recordedTime;
    }
    public String getUserId() {
        return userId;
    }
    public String setUserId() {this.userId = userId;
        return userId;
    }
    public String getTotalTime() {
        return totalTime;
    }
    public int getTotalEvent() { return totalEvent;
    }

    public ReadData(int eventBad, String recordedTime, String userId) {
        this.eventBad = eventBad;
        this.recordedTime = recordedTime;
        this.userId = userId;
    }

}

