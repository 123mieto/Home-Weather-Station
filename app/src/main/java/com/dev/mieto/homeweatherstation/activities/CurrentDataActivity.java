package com.dev.mieto.homeweatherstation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.dev.mieto.homeweatherstation.DonutGraphView;
import com.dev.mieto.homeweatherstation.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CurrentDataActivity extends AppCompatActivity {

    public static final String TAG = MeasActivity.class.getSimpleName();
    public static final String ENDPOINT = "http://192.168.0.87:5000/api/v1/";
    public static final String ENDPOINT_ESP8266 = "http://192.168.0.80:80/";
    private OkHttpClient okClient;

    private Toolbar mToolbar;
    private Request currLightLevel;
    private Request currTemp;

    private String currLightLvlVal;
    private String currTempVal;

    private DonutGraphView donutGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_data);
        setToolbar();
        donutGraphView = (DonutGraphView) findViewById(R.id.donut);
        getCurrentLightLevel();
        getCurrentTemperature();
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

    private void getCurrentLightLevel() {
        okClient = new OkHttpClient();
        currLightLevel = new Request.Builder().url(ENDPOINT_ESP8266 + "light_lvl").build();

        /*Request to check connection with esp*/
        okClient.newCall(currLightLevel).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Toast.makeText(CurrentDataActivity.this, "Error connecting ESP8266", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.body() != null) {
                    currLightLvlVal = response.body().toString();
                }
            }
        });

//        donutGraphView.setWidth(100);
//        donutGraphView.setHeight(100);
//        donutGraphView.invalidate();
    }

    private void getCurrentTemperature() {
        okClient = new OkHttpClient();
        currTemp = new Request.Builder().url(ENDPOINT_ESP8266 + "temp").build();

        /*Request to check connection with esp*/
        okClient.newCall(currTemp).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Toast.makeText(CurrentDataActivity.this, "Error connecting ESP8266", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.body() != null) {
                    currTempVal = response.body().toString();
                }
            }
        });
    }
}
