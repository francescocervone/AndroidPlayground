package com.francescocervone.movies.data.entities;

import com.google.gson.annotations.SerializedName;

public class GenreEntity {
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;

    public String getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

}
