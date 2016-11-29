package com.dev.mieto.homeweatherstation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dev.mieto.homeweatherstation.DataBaseHelper;
import com.dev.mieto.homeweatherstation.DayDataTemp;
import com.dev.mieto.homeweatherstation.DayDataTempResult;
import com.dev.mieto.homeweatherstation.DayViewAdapter;
import com.dev.mieto.homeweatherstation.R;
import com.dev.mieto.homeweatherstation.RestService;
import com.dev.mieto.homeweatherstation.activities.DaysActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mieto on 2016-10-30.
 */
public class TempDaysFragment extends android.support.v4.app.Fragment {

    /*Tag do debugu*/
    public static final String TAG = DaysActivity.class.getSimpleName();
    private RecyclerView recyclerView;

    public TempDaysFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareRetrofit();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_days_temp,container,false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.days_recycler_view);
        return layout;
    }

    private void prepareRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DaysActivity.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestService restService = retrofit.create(RestService.class);
        restService.getDaysTemp().enqueue(new Callback<DayDataTempResult>() {

            @Override
            public void onResponse(Call<DayDataTempResult> call, Response<DayDataTempResult> response) {
                Log.d(TAG, "onResponse: OK ");
                if (isAdded()){
                    DayViewAdapter measAdapter = new DayViewAdapter(response.body().getDays());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(measAdapter);

                    DataBaseHelper db = new DataBaseHelper(getContext());
                    ArrayList<DayDataTemp> oldDataList = db.selectTemperatureData();

                    if (oldDataList.size() > 0) {
                        for (DayDataTemp data : response.body().getDays()) {
                            for (DayDataTemp oldData : oldDataList) {
                                if ((data.getDate().equals(oldData.getDate()))) {
                                    //Values are sorted so next values are also repeated
                                    break;
                                }
                            }
                            //No such data found so put it into local db
                            db.insertTemperatureData(data.getTemperatures(),
                                    data.getTimes(),
                                    data.getDate());
                        }
                    } else {
                        //No old data found so put everything in local db
                        for (DayDataTemp data : response.body().getDays()) {
                            db.insertTemperatureData(data.getTemperatures(),
                                    data.getTimes(),
                                    data.getDate());
                        }
                    }
                }
             }

            @Override
            public void onFailure(Call<DayDataTempResult> call, Throwable t) {
                Log.e(TAG, "onFailure: NI MA NETU " + t.getMessage(), t);
                if (isAdded()){
                    Toast.makeText(getContext(), "Error downloading. Data not refreshed", Toast.LENGTH_LONG).show();
                    DataBaseHelper db = new DataBaseHelper(getContext());
                    DayViewAdapter measAdapter = new DayViewAdapter(db.selectTemperatureData());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(measAdapter);
                }
            }
        });

    }
}
