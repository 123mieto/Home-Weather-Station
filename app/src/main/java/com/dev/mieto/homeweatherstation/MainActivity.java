package com.dev.mieto.homeweatherstation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Mieto on 2016-09-11.
 */
public class MainActivity extends AppCompatActivity {

    Button allMeasButton;
    Button allDaysButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allMeasButton = (Button) findViewById(R.id.all_meas);
        allDaysButton = (Button) findViewById(R.id.all_days);
    }

    public void clickedAllMeasButton(View view){
        Intent intent = new Intent(this, MeasActivity.class);
        startActivity(intent);
    }

    public void clickedAllDaysButton(View view){
        Intent intent = new Intent(this, DaysActivity.class);
        startActivity(intent);
    }

}
