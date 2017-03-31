package com.francescocervone.movies.data.entities;

import com.google.gson.annotations.SerializedName;

public class SpokenLanguageEntity {

    @SerializedName("name")
    private String mName;
    @SerializedName("iso_639_1")
    private String mIso6391;

    public String getName() {
        return this.mName;
    }

    public String getIso6391() {
        return this.mIso6391;
    }


}
