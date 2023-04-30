package com.example.le_androidapp.tables;

public class Mode {
    public String modeId;
    private String userId;
    public Boolean isWorkingMode;

    public String getModeId() {
        return modeId;
    }

    public Boolean getIsWorkingMode() {
        return isWorkingMode;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Mode(String userId, String modeId, Boolean isWorkingMode) {
        this.modeId = modeId;
        this.isWorkingMode = isWorkingMode;
        this.userId = userId;
    }
}
