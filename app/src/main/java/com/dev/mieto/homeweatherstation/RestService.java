package com.dev.mieto.homeweatherstation;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.GET;

/**
 * Created by Mieto on 2016-09-06.
 */
public interface RestService {

    @GET("/temperature/api/v1/readings")
    Call<MeasurementResult> getResults();

}
