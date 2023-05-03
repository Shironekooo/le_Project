package com.example.le_androidapp.tables;

public class History {

    public String userId;
    public String totalEvent;
    public String totalTime;
    String dateData;



    public History() {
        // Required empty constructor for Firebase
    }

    public History(String userId) {

    }

    public String getHistoryId() {
        return userId;
    }
    public String getDateData() { return dateData; }
    public String getTotalEvent() {return totalEvent; }

    public String getTotalTime() {
        return totalTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public History(String totalEvent, String totalTime,String userId) {
        this.totalEvent = totalEvent;
        this.totalTime = totalTime;
        this.userId = userId;
    }
}
