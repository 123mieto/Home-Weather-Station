package com.dev.mieto.homeweatherstation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dev.mieto.homeweatherstation.DataBaseHelper;
import com.dev.mieto.homeweatherstation.DayDataLight;
import com.dev.mieto.homeweatherstation.DayDataLightResult;
import com.dev.mieto.homeweatherstation.DayLightAdapter;
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
public class LightDaysFragment extends Fragment {
    public static final String TAG = DaysActivity.class.getSimpleName();
    private RecyclerView recyclerView;

    public LightDaysFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareRetrofit();
    }

    private void prepareRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DaysActivity.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestService restService = retrofit.create(RestService.class);
        restService.getDaysLight().enqueue(new Callback<DayDataLightResult>() {
           @Override
           public void onResponse(Call<DayDataLightResult> call, Response<DayDataLightResult> response) {
               Log.d(TAG, "onResponse: OK ");
               if (isAdded()){
                   DayLightAdapter measAdapter = new DayLightAdapter(response.body().getDays());

                   recyclerView.setHasFixedSize(true);
                   recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                   recyclerView.setAdapter(measAdapter);

                   DataBaseHelper db = new DataBaseHelper(getContext());
                   ArrayList<DayDataLight> oldDataList = db.selectLightData();

                   if (oldDataList.size() > 0) {
                       for (DayDataLight data : response.body().getDays()) {
                           for (DayDataLight oldData : oldDataList) {
                               if ((data.getDate() == oldData.getDate())) {
                                   //Values are sorted so next values are also repeated
                                   break;
                               }
                           }
                           //No such data found so put it into local db
                           db.insertLightData(data.getLight(),
                                   data.getTimes(),
                                   data.getDate());
                       }
                   } else {
                       //No old data found so put everything in local db
                       for (DayDataLight data : response.body().getDays()) {
                           db.insertLightData(data.getLight(),
                                   data.getTimes(),
                                   data.getDate());
                       }
                   }
               }
           }

           @Override
           public void onFailure(Call<DayDataLightResult> call, Throwable t) {
               Log.e(TAG, "onFailure: NI MA NETU " + t.getMessage(), t);
               if (isAdded()) {
                   Toast.makeText(getContext(), "Error downloading. Data not refreshed", Toast.LENGTH_LONG).show();
                   DataBaseHelper db = new DataBaseHelper(getContext());
                   DayLightAdapter measAdapter = new DayLightAdapter(db.selectLightData());
                   recyclerView.setHasFixedSize(true);
                   recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                   recyclerView.setAdapter(measAdapter);
               }
           }
       }

    );

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_days_light, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.light_recycler_view);
        return layout;
    }


}

