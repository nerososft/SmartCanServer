package com.iot.nero.smartcan.entity;

import com.iot.nero.smartcan.core.Protocol;

import java.io.Serializable;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2018/8/3
 * Time   3:09 PM
 */
public class Tick implements Serializable {
    private Protocol protocol;
    private Long last;

    public Tick() {
    }

    public Tick(Protocol protocol, Long last) {
        this.protocol = protocol;
        this.last = last;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Long getLast() {
        return last;
    }

    public void setLast(Long last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return "Tick{" +
                "protocol=" + protocol +
                ", last=" + last +
                '}';
    }
}
