package com.francescocervone.movies.data.entities;

import com.google.gson.annotations.SerializedName;

public class DatesEntity {
    @SerializedName("maximum")
    private String mMaximum;
    @SerializedName("minimum")
    private String mMinimum;

    public DatesEntity(String maximum, String minimum) {
        mMaximum = maximum;
        mMinimum = minimum;
    }

    public String getMaximum() {
        return mMaximum;
    }

    public String getMinimum() {
        return mMinimum;
    }
}
