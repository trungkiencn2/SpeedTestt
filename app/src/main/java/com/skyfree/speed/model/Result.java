package com.skyfree.speed.model;

/**
 * Created by KienBeu on 4/20/2018.
 */

public class Result {
    private Integer id;
    private Integer type;
    private String date;
    private String speedDownload, speedUpload;
    private String ping;

    public Result() {
    }

    public int getId() {
        return id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpeedDownload() {
        return speedDownload;
    }

    public void setSpeedDownload(String speedDownload) {
        this.speedDownload = speedDownload;
    }

    public String getSpeedUpload() {
        return speedUpload;
    }

    public void setSpeedUpload(String speedUpload) {
        this.speedUpload = speedUpload;
    }

    public String getPing() {
        return ping;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    public Result(Integer type, String date, String speedDownload, String speedUpload, String ping) {
        this.type = type;
        this.date = date;
        this.speedDownload = speedDownload;
        this.speedUpload = speedUpload;
        this.ping = ping;
    }
}
