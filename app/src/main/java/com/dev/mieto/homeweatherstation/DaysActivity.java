package com.dev.mieto.homeweatherstation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DaysActivity extends AppCompatActivity {

    /*Tag do debugu*/
    public static final String TAG = DaysActivity.class.getSimpleName();
    public static final String ENDPOINT = "http://192.168.0.87:5000";

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);
        recyclerView = (RecyclerView) findViewById(R.id.days_recycler_view);
        prepareRetrofit();
    }

    private void prepareRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestService restService = retrofit.create(RestService.class);
        restService.getDays().enqueue(new Callback<DayDataResult>() {

            @Override
            public void onResponse(Call<DayDataResult> call, Response<DayDataResult> response) {
                Log.d(TAG, "onResponse: OK ");
                DayViewAdapter measAdapter = new DayViewAdapter(response.body().days);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(DaysActivity.this));
                recyclerView.setAdapter(measAdapter);
            }

            @Override
            public void onFailure(Call<DayDataResult> call, Throwable t) {
                Log.e(TAG, "onFailure: NI MA NETU " + t.getMessage(), t);
                Toast.makeText(DaysActivity.this, "Error downloading", Toast.LENGTH_LONG).show();
            }
        });

    }
}
