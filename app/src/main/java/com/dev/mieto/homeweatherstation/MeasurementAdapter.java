package com.dev.mieto.homeweatherstation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mieto on 2016-09-06.
 */
public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.MeasurementViewHolder>  {

    private List<Measurement> mMeasurementList;

    public MeasurementAdapter(List<Measurement> readings) {
        this.mMeasurementList = readings;
    }

    @Override
    public MeasurementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.measurement_item, parent, false);
        return new MeasurementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeasurementViewHolder holder, int position) {
        holder.number.setText(mMeasurementList.get(position).getNumber());
        holder.temperature.setText(mMeasurementList.get(position).getTemperature());
        holder.time.setText(mMeasurementList.get(position).getTime());
        holder.date.setText(mMeasurementList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mMeasurementList.size();
    }

    public static class MeasurementViewHolder extends RecyclerView.ViewHolder {

        TextView number, temperature, time, date;

        public MeasurementViewHolder(View itemView) {
            super(itemView);

            number = (TextView) itemView.findViewById(R.id.meas_item_num);
            temperature = (TextView) itemView.findViewById(R.id.meas_item_temp);
            time = (TextView) itemView.findViewById(R.id.meas_item_time);
            date = (TextView) itemView.findViewById(R.id.meas_item_date);

        }
    }
}
