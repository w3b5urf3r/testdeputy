package com.deputy.test.mariolopez.beans;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by mario on 23/02/2017.
 */

public class ShiftInfo {

    @SerializedName("time")
    private Date time;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;
    private boolean started = false;

    public ShiftInfo(Date date, double latitude, double longitude) {
        this.time = date;
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }


    public Date getTime() {
        return time;
    }


    public static String formatTime(boolean isStarted, Date time) {
        if (isStarted) {
            return "started at : " + time.toString();
        }
        return "ended at : " + time.toString();
    }
}
