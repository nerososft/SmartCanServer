package com.iot.nero.smartcan.utils.cache;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2018/8/5
 * Time   6:50 PM
 */
public class PushBean implements Serializable {
    private byte[] uniqueId;
    private String email;
    private byte[] code;
    private Integer times;

    public PushBean() {
    }

    public PushBean(byte[] uniqueId, String email, byte[] code, Integer times) {
        this.uniqueId = uniqueId;
        this.email = email;
        this.code = code;
        this.times = times;
    }

    public byte[] getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(byte[] uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "PushBean{" +
                "uniqueId='" + uniqueId + '\'' +
                ", email='" + email + '\'' +
                ", code=" + Arrays.toString(code) +
                ", times=" + times +
                '}';
    }
}
