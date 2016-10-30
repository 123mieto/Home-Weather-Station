package com.dev.mieto.homeweatherstation;

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

    public LightDaysFragment() {}

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
                DayLightAdapter measAdapter = new DayLightAdapter(response.body().getDays());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(measAdapter);
            }

            @Override
            public void onFailure(Call<DayDataLightResult> call, Throwable t) {
                Log.e(TAG, "onFailure: NI MA NETU " + t.getMessage(), t);
                Toast.makeText(getContext(), "Error downloading", Toast.LENGTH_LONG).show();
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_days_light, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.light_recycler_view);
        return layout;
    }


}

