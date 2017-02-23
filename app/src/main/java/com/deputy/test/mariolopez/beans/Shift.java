package com.deputy.test.mariolopez.beans;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by mario on 23/02/2017.
 */

public class Shift {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";
    @SerializedName("id")
    private int id;

    @SerializedName("start")
    private Date start;

    @SerializedName("end")
    private Date end;

    @SerializedName("startLatitude")
    private String startLatitude;

    @SerializedName("startLongitude")
    private String startLongitude;

    @SerializedName("endLatitude")
    private String endLatitude;

    @SerializedName("endLongitude")
    private String endLongitude;

    @SerializedName("image")
    private String imageUrl;

    //Should use string format for localisation..
    public static String getFormattedLatitude(Shift shift) {
        return "start lat: " + shift.startLatitude + "end lat: " + shift.endLatitude;
    }

    public static String getFormattedLongitude(Shift shift) {
        return "start lat: " + shift.startLongitude + "end lat: " + shift.endLongitude;
    }

    public int getId() {
        return id;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
