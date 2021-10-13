package com.robertory.packinglot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WaziupApi {
    @GET("sensors")
    Call<List<Sensor>> getSensors();
}
