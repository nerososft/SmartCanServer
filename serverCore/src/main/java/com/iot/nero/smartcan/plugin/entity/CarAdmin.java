package com.iot.nero.smartcan.plugin.entity;

import java.io.Serializable;

public class CarAdmin implements Serializable {
    private Integer userId;

    private String uniqueId;

    private Integer isPhoneOpen;

    private Integer isMailOpen;

    public CarAdmin() {
    }

    public CarAdmin(Integer userId, String uniqueId, Integer isPhoneOpen, Integer isMailOpen) {
        this.userId = userId;
        this.uniqueId = uniqueId;
        this.isPhoneOpen = isPhoneOpen;
        this.isMailOpen = isMailOpen;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getIsPhoneOpen() {
        return isPhoneOpen;
    }

    public void setIsPhoneOpen(Integer isPhoneOpen) {
        this.isPhoneOpen = isPhoneOpen;
    }

    public Integer getIsMailOpen() {
        return isMailOpen;
    }

    public void setIsMailOpen(Integer isMailOpen) {
        this.isMailOpen = isMailOpen;
    }

    @Override
    public String toString() {
        return "CarAdmin{" +
                "userId=" + userId +
                ", uniqueId='" + uniqueId + '\'' +
                ", isPhoneOpen=" + isPhoneOpen +
                ", isMailOpen=" + isMailOpen +
                '}';
    }
}
