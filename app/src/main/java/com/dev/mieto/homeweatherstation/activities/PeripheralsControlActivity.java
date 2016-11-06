package com.dev.mieto.homeweatherstation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.mieto.homeweatherstation.ConnHwResult;
import com.dev.mieto.homeweatherstation.LedResult;
import com.dev.mieto.homeweatherstation.LogResult;
import com.dev.mieto.homeweatherstation.R;
import com.dev.mieto.homeweatherstation.RestService;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PeripheralsControlActivity extends AppCompatActivity {

    public static final String TAG = MeasActivity.class.getSimpleName();
    public static final String ENDPOINT = "http://192.168.0.87:5000/api/v1/";
    public static final String ENDPOINT_ESP8266 = "http://192.168.0.80:80/";

    private TextView mTvLed;
    private TextView mTvESPLed;
    private Switch mSwLed;
    private Switch mESPLEd;
    private Toolbar mToolbar;

    private int ledNumber;
    private long time, duration, deviceNumber;

    private RestService mRestService;
    private OkHttpClient okClient;
    //TODO: pomiar - inny plik
//    private Request tempReqESP;
//    private Request lightReqESP;
    private Request ledOnESP;
    private Request ledOffESP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripherals_controll);

        mTvLed = (TextView) findViewById(R.id.tvLedSwith);
        mSwLed = (Switch) findViewById(R.id.swLed1);
        mTvESPLed = (TextView) findViewById(R.id.tvESPLed);
        mESPLEd = (Switch) findViewById(R.id.swLed2);

        //STUB
        ledNumber = 1;
        time = 999;
        duration = 999;
        deviceNumber = 1;

        setToolbar();

        prepareRetrofit();
        prepareOkHttpClient();
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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

    private void prepareOkHttpClient() {
        okClient = new OkHttpClient();
        ledOnESP = new Request.Builder().url(ENDPOINT_ESP8266 + "ledOn").build();
        ledOffESP = new Request.Builder().url(ENDPOINT_ESP8266 + "ledOff").build();
        /*Request to check connection with esp*/
        okClient.newCall(new Request.Builder().url(ENDPOINT_ESP8266).build()).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                mESPLEd.setEnabled(false);
                mTvESPLed.setEnabled(false);
                Toast.makeText(PeripheralsControlActivity.this, "Error connecting ESP8266", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                mESPLEd.setEnabled(true);
                mTvESPLed.setEnabled(true);
            }
        });

        //TODO: powinno byc w innym pliku a nie w kontrol - to jest pobieranie pomiaru
//        tempReqESP = new Request.Builder().url(ENDPOINT_ESP8266 + "temp").build();
//        lightReqESP = new Request.Builder().url(ENDPOINT_ESP8266 + "light_lvl").build();
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
                Toast.makeText(PeripheralsControlActivity.this, "Error connecting raspberry", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PeripheralsControlActivity.this, "Error connecting raspberry", Toast.LENGTH_LONG).show();
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

    public void switchESPLed(boolean state) {
        Request req;
        if (state) {
            req = ledOnESP;
        } else {
            req = ledOffESP;
        }

        okClient.newCall(req).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error: " + response, Toast.LENGTH_LONG).show();
                } else {
                    //GOOD
                }
            }
        });
    }

    public void onDirectESP8266LedSwitch(View view) {
        if (mESPLEd.isChecked()) {
            switchESPLed(true);
        } else {
            switchESPLed(false);
        }
    }
}
