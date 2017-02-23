package com.deputy.test.mariolopez.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mario on 23/02/2017.
 */

public class BusinessInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("logoUrl")
    private String logoURl;

    public String getName() {
        return name;
    }

    public String getLogoURl() {
        return logoURl;
    }
}
