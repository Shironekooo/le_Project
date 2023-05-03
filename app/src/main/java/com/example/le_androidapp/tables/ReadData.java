package com.example.le_androidapp.tables;

public class ReadData {

    int eventBad;
    String recordedTime;
    String userId;

    public ReadData(String userId, int i, String formattedTime) {
    }


    public ReadData(String userId) {
        // Default constructor required for calls to DataSnapshot.getValue(ReadData.class)
    }

    public ReadData(int eventBad, String recordedTime, String userId) {

        this.eventBad = eventBad;
        this.recordedTime = recordedTime;
        this.userId = userId;
    }

    //Getters and Setters
    public int getEventBad() {
        return eventBad;
    }

    public void setEventBad(int eventBad) {
        this.eventBad = eventBad;
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
    public void setUserId(String userId) {
        this.userId = userId;
    }


}

