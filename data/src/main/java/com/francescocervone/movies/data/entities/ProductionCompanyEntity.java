package com.francescocervone.movies.data.entities;

import com.google.gson.annotations.SerializedName;

public class ProductionCompanyEntity {
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;

    public String getName() {
        return this.mName;
    }

    public String getId() {
        return this.mId;
    }


}
