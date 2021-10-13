package com.robertory.packinglot;

import com.google.gson.annotations.SerializedName;

public class Value {
    @SerializedName("date_received")
    private String date;
    @SerializedName("value")
    private String value;

    public Value(String date, String value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public String getValue() {
        return value;
    }
}
