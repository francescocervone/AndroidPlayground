package com.francescocervone.movies.data.entities;

import com.google.gson.annotations.SerializedName;

public class ProductionCountryEntity {

    @SerializedName("name")
    private String mName;
    @SerializedName("iso_3166_1")
    private String mIso31661;

    public String getName() {
        return this.mName;
    }

    public String getIso31661() {
        return this.mIso31661;
    }


}
