package com.robertory.packinglot;

import com.google.gson.annotations.SerializedName;

public class Sensor {
    private String name;
    private String id;
    private String unit;
    @SerializedName("value")
    private Value value;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUnit() {
        return unit;
    }

    public Value getValue() {
        return value;
    }

    public Sensor(String id, String name, Value value, String unit) {
        this.name = name;
        this.id = id;
        this.unit = unit;
        this.value=value;
    }
}
