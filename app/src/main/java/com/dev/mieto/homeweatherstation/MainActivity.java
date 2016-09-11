package com.dev.mieto.homeweatherstation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    /*Tag do debugu*/
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String ENDPOINT = "http://192.168.0.87:5000";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.star_recycler_view);
        prepareRetrofit();

    }

    private void prepareRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestService restService = retrofit.create(RestService.class);
        restService.getResults().enqueue(new Callback<MeasurementResult>() {

            @Override
            public void onResponse(Call<MeasurementResult> call, Response<MeasurementResult> response) {
                Log.d(TAG, "onResponse: OK ");
                MeasurementAdapter measAdapter = new MeasurementAdapter(response.body().readings);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(measAdapter);
            }

            @Override
            public void onFailure(Call<MeasurementResult> call, Throwable t) {
                Log.e(TAG, "onFailure: NI MA NETU " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Error downloading", Toast.LENGTH_LONG).show();
            }
        });

    }
}
