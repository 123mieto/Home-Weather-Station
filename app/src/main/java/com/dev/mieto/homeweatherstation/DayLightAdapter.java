package com.dev.mieto.homeweatherstation;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mieto on 2016-10-30.
 */
public class DayLightAdapter extends RecyclerView.Adapter<DayViewAdapter.DayViewViewHolder> {

    private List<DayDataLight> mDayData = new ArrayList<>();

    private long[] mTimesREST;
    private int[] mLightsREST;

    public DayLightAdapter(List<DayDataLight> results) {
        /*Show only those results that collected any data*/
        for (DayDataLight result : results) {
            if (result.getLight().length > 0) {
                this.mDayData.add(result);
            }
        }
    }

    private LineSet prepChartDataset() {
        LineSet chartDataset = new LineSet(new String[]{"", ""}, new float[]{0f, 20.0f});
        return chartDataset;
    }

    @Override
    public DayViewAdapter.DayViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new DayViewAdapter.DayViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayViewAdapter.DayViewViewHolder holder, int position) {
        holder.date.setText(mDayData.get(position).getDate());
        mTimesREST = mDayData.get(position).getTimes();
        mLightsREST = mDayData.get(position).getLight();
        int mTempRestLen = mLightsREST.length;
        if (mTempRestLen > 0) {
            float[] tempRESTfloat = new float[mTempRestLen];
            String[] tempRESTstr = new String[mTempRestLen];
            for (int i = 0; i < mTempRestLen; i++) {
                tempRESTfloat[i] = (float) mLightsREST[i];
                tempRESTstr[i] = "";
            }

            LineSet dataset = new LineSet(tempRESTstr, tempRESTfloat);
            dataset.setColor(Color.parseColor("#758cbb"))
                    .setDotsColor(Color.parseColor("#758cbb"))
                    .setThickness(4)
                    .setDashed(new float[]{10f, 10f});
            holder.measChart.addData(dataset);

            holder.measChart.setBorderSpacing(Tools.fromDpToPx(15))
                    .setAxisBorderValues(0, 1025)
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
}
