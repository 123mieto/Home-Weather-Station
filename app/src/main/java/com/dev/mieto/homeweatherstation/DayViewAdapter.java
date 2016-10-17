package com.dev.mieto.homeweatherstation;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mieto on 2016-09-11.
 */
public class DayViewAdapter extends RecyclerView.Adapter<DayViewAdapter.DayViewViewHolder> {

    private List<DayData> mDayData = new ArrayList<>();

    private long[] mTimesREST;
    private int[] mTemperaturesREST;

    public DayViewAdapter(List<DayData> results) {
        /*Show only those results that collected any data*/
        for(DayData result : results){
            if (result.getTemperatures().length > 0){
                this.mDayData.add(result);
            }
        }
    }

    private LineSet prepChartDataset() {
        LineSet chartDataset = new LineSet(new String[]{"", ""}, new float[]{0f, 20.0f});

        return chartDataset;
    }

    @Override
    public DayViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new DayViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayViewViewHolder holder, int position) {
        holder.date.setText(mDayData.get(position).getDate());
        mTimesREST = mDayData.get(position).getTimes();
        mTemperaturesREST = mDayData.get(position).getTemperatures();
        int mTempRestLen = mTemperaturesREST.length;
        if (mTempRestLen > 0) {
            float[] tempRESTfloat = new float[mTempRestLen];
            String[] tempRESTstr = new String[mTempRestLen];
            for (int i = 0; i < mTempRestLen; i++) {
                tempRESTfloat[i] = (float) mTemperaturesREST[i];
                tempRESTstr[i] = "";
            }

            LineSet dataset = new LineSet(tempRESTstr, tempRESTfloat);
            dataset.setColor(Color.parseColor("#758cbb"))
                    .setDotsColor(Color.parseColor("#758cbb"))
                    .setThickness(4)
                    .setDashed(new float[]{10f, 10f});
            holder.measChart.addData(dataset);

            holder.measChart.setBorderSpacing(Tools.fromDpToPx(15))
                    .setAxisBorderValues(0, 30)
                    .setYLabels(AxisController.LabelPosition.NONE)
                    .setLabelsColor(Color.parseColor("#6a84c3"))
                    .setXAxis(false)
                    .setYAxis(false);

            holder.measChart.show();

        }

    }

    @Override
    public int getItemCount() {
        return mDayData.size();
    }

    public static class DayViewViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        LineChartView measChart;

        public DayViewViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.tv_date_text);
            measChart = (LineChartView) itemView.findViewById(R.id.day_temp_chart);
        }
    }
}
