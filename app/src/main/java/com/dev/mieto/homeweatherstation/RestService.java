package com.dev.mieto.homeweatherstation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Mieto on 2016-09-06.
 */
public interface RestService {

    @GET("temperature/readings")
    Call<MeasurementResult> getResults();

    @GET("temperature/days")
    Call<DayDataResult> getDays();

    @GET("controller/connected-hardware")
    Call<ConnHwResult> getConnHardware();

    @GET("controller/leds")
    Call<ConnHwResult> getLedsStatus();

    @POST("controller/leds")
    Call<LedResult> setLed(
            @Query("number") int number,
            @Query("state") int state);

    //Used for logging time when app is connected to db
    @POST("logging/app-sync")
    Call<LogResult> logTime(
            @Query(value = "time") long time,
            @Query(value = "device") long device,
            @Query(value = "duration") long duration);
}

