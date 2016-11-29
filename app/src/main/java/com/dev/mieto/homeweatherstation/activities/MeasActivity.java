package com.dev.mieto.homeweatherstation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.dev.mieto.homeweatherstation.MeasurementAdapter;
import com.dev.mieto.homeweatherstation.MeasurementResult;
import com.dev.mieto.homeweatherstation.R;
import com.dev.mieto.homeweatherstation.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeasActivity extends AppCompatActivity {

    /*Tag do debugu*/
    public static final String TAG = MeasActivity.class.getSimpleName();
    public static final String ENDPOINT = "http://192.168.0.87:5000/api/v1/";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meas);
        recyclerView = (RecyclerView) findViewById(R.id.star_recycler_view);
        setToolbar();
        prepareRetrofit();

    }

    private void setToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
                MeasurementAdapter measAdapter = new MeasurementAdapter(response.body().getReadings());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MeasActivity.this));
                recyclerView.setAdapter(measAdapter);
            }

            @Override
            public void onFailure(Call<MeasurementResult> call, Throwable t) {
                Log.e(TAG, "onFailure: NI MA NETU " + t.getMessage(), t);
                Toast.makeText(MeasActivity.this, "Error downloading", Toast.LENGTH_LONG).show();
            }
        });

    }
}
