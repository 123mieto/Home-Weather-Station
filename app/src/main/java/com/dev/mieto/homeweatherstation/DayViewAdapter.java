package com.dev.mieto.homeweatherstation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.view.LineChartView;

import java.util.List;

/**
 * Created by Mieto on 2016-09-11.
 */
public class DayViewAdapter extends RecyclerView.Adapter<DayViewAdapter.DayViewViewHolder> {

    List<DayData> mDayData;

    public DayViewAdapter(List<DayData> results) {
        this.mDayData = results;
    }

    @Override
    public DayViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new DayViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayViewViewHolder holder, int position) {
        holder.date.setText(mDayData.get(position).date);
        //todo: dodac measChart
    }

    @Override
    public int getItemCount() {
        return mDayData.size();
    }

    public static class DayViewViewHolder extends RecyclerView.ViewHolder{

        TextView date;
        LineChartView measChart;

        public DayViewViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.tv_date_text);
            measChart = (LineChartView) itemView.findViewById(R.id.day_temp_chart);
        }
    }
}
