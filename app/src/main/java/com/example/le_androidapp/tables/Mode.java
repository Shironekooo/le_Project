package com.example.le_androidapp.tables;

public class Mode {
    public String modeId;
    private String userId;
    public Boolean restingMode;
    public Boolean workingMode;

    public String getModeId() {
        return modeId;
    }

    public Boolean getRestingMode() {
        return restingMode;
    }

    public Boolean getWorkingMode() {
        return workingMode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Mode(String userId, String modeId, Boolean restingMode, Boolean workingMode) {
        this.modeId = modeId;
        this.restingMode = restingMode;
        this.workingMode = workingMode;
        this.userId = userId;
    }
}
