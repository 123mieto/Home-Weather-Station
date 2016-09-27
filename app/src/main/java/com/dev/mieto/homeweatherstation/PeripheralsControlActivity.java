package com.dev.mieto.homeweatherstation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PeripheralsControlActivity extends AppCompatActivity {

    public static final String TAG = MeasActivity.class.getSimpleName();
    public static final String ENDPOINT = "http://192.168.0.87:5000/api/v1/";

    private TextView mTvLed;
    private Switch mSwLed;

    private int ledNumber;
    private long time, duration, deviceNumber;

    private RestService mRestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripherals_controll);

        mTvLed = (TextView) findViewById(R.id.tvLedSwith);
        mSwLed = (Switch) findViewById(R.id.swLed1);

        //STUB
        ledNumber = 1;
        time = 999;
        duration = 999;
        deviceNumber = 1;

        prepareRetrofit();
    }

    private void prepareRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestService = retrofit.create(RestService.class);
        mRestService.getConnHardware().enqueue(new Callback<ConnHwResult>() {

            @Override
            public void onResponse(Call<ConnHwResult> call, Response<ConnHwResult> response) {

            }

            @Override
            public void onFailure(Call<ConnHwResult> call, Throwable t) {
                Log.e(TAG, "onFailure: NI MA NETU " + t.getMessage(), t);
                Toast.makeText(PeripheralsControlActivity.this, "Error downloading", Toast.LENGTH_LONG).show();
            }
        });

        mRestService.getLedsStatus().enqueue(new Callback<ConnHwResult>() {

            @Override
            public void onResponse(Call<ConnHwResult> call, Response<ConnHwResult> response) {
                //Disable switches if no hardware
                if (response.body().getHwList() != null) {
                    mSwLed.setEnabled(true);
                    mTvLed.setEnabled(true);
                } else {
                    mSwLed.setEnabled(false);
                    mTvLed.setEnabled(false);
                }

            }

            @Override
            public void onFailure(Call<ConnHwResult> call, Throwable t) {
                Log.e(TAG, "onFailure: NI MA NETU " + t.getMessage(), t);
                Toast.makeText(PeripheralsControlActivity.this, "Error downloading", Toast.LENGTH_LONG).show();
                //Disable switches if no connection
                mSwLed.setEnabled(false);
                mTvLed.setEnabled(false);
            }
        });
    }

    public void logConnectionAPI() {
        //TODO: to nie bedzie wołane z tego miejsca
        Call<LogResult> logCall = mRestService.logTime(time, deviceNumber, duration);
        logCall.enqueue(new Callback<LogResult>() {
            @Override
            public void onResponse(Call<LogResult> call, Response<LogResult> response) {

            }

            @Override
            public void onFailure(Call<LogResult> call, Throwable t) {

            }
        });
    }

    public void switchLedAPI(int state) {
        //TODO: to nie bedzie wołane z tego miejsca

        Call<LedResult> ledCall = mRestService.setLed(ledNumber, state);
        ledCall.enqueue(new Callback<LedResult>() {
            @Override
            public void onResponse(Call<LedResult> call, Response<LedResult> response) {

            }

            @Override
            public void onFailure(Call<LedResult> call, Throwable t) {
            }
        });


    }

    public void onLedSwitch(View view) {
        if (mSwLed.isChecked()) {
            switchLedAPI(1);
        } else {
            switchLedAPI(0);
        }
    }
}
