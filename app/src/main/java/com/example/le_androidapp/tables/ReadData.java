package com.example.le_androidapp.tables;

public class ReadData {

    String dataId;
    int eventBad;
    long recordedTime;

    public ReadData() {
        // Default constructor required for calls to DataSnapshot.getValue(ReadData.class)
    }

    public ReadData(String dataId, int eventBad, long recordedTime) {

        this.eventBad = eventBad;
        this.recordedTime = recordedTime;
        this.dataId = dataId;
    }

    //Getters and Setters
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

}

