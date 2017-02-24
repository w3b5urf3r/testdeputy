package com.deputy.test.mariolopez.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by mario on 23/02/2017.
 */

public class Shift implements Parcelable {

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

    public static Double getCoordinate(String coordinate) {
        return Double.valueOf(coordinate);
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

    //Should use string format for localisation..
    public static String getFormattedLatitude(Shift shift) {
        return "start lat: " + shift.startLatitude + " end lat: " + shift.endLatitude;
    }

    public static String getFormattedLongitude(Shift shift) {
        return "start long: " + shift.startLongitude + " end long: " + shift.endLongitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.start != null ? this.start.getTime() : -1);
        dest.writeLong(this.end != null ? this.end.getTime() : -1);
        dest.writeString(this.startLatitude);
        dest.writeString(this.startLongitude);
        dest.writeString(this.endLatitude);
        dest.writeString(this.endLongitude);
        dest.writeString(this.imageUrl);
    }

    public Shift() {
    }

    protected Shift(Parcel in) {
        this.id = in.readInt();
        long tmpStart = in.readLong();
        this.start = tmpStart == -1 ? null : new Date(tmpStart);
        long tmpEnd = in.readLong();
        this.end = tmpEnd == -1 ? null : new Date(tmpEnd);
        this.startLatitude = in.readString();
        this.startLongitude = in.readString();
        this.endLatitude = in.readString();
        this.endLongitude = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Creator<Shift> CREATOR = new Creator<Shift>() {
        @Override
        public Shift createFromParcel(Parcel source) {
            return new Shift(source);
        }

        @Override
        public Shift[] newArray(int size) {
            return new Shift[size];
        }
    };
}
