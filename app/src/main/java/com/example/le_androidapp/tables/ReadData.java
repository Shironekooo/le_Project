package com.example.le_androidapp.tables;

public class ReadData {

    public int eventBad;
    public String recordedTime;
    public String userId;
    public static int totalEvent;
    public int totalTime;
    private String dailyEvent;

    public ReadData() {

    }

    public ReadData(String userId) {
        this.userId=userId;
    }

    public ReadData(String userId, String s, String s1) {
    }
    public String getDailyEvent(){
        return dailyEvent;
    }
    public void setDailyEvent(int totalEvent){
        this.dailyEvent = String.valueOf(totalEvent);
    }

    public int getEventBad() {
        return eventBad;
    }

    public void setTotalEvent(int totalEvent) {
        this.totalEvent = totalEvent;
    }
    public int getTotalEvent(int totalEvent) {
        return totalEvent;
    }

    public void setTotalTime(int totalTime) {
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
    public int getTotalTime() {
        return totalTime;
    }
    public int getTotalEvent() { return totalEvent;
    }

    public ReadData(int eventBad, String recordedTime, String userId, int totalEvent, int totalTime) {
        this.eventBad = eventBad;
        this.recordedTime = recordedTime;
        this.userId = userId;
        this.totalEvent = totalEvent;
        this.totalTime = totalTime;
    }

    public static int getTotalEvent(Object o) {return totalEvent;
    }


}

