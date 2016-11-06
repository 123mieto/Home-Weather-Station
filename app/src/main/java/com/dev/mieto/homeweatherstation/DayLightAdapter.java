package com.dev.mieto.homeweatherstation;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;

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
            //prepare colors
            int dotColor = holder.measChart.getResources().getColor(R.color.accent);
            int gridLabelColor = holder.measChart.getResources().getColor(R.color.secondary_text);

            Paint gridPaint = new Paint();
            gridPaint.setColor(gridLabelColor);
            gridPaint.setStyle(Paint.Style.STROKE);
            gridPaint.setAntiAlias(true);
            gridPaint.setStrokeWidth(1);

            LineSet dataset = new LineSet(tempRESTstr, tempRESTfloat);
            dataset.setColor(dotColor)
                    .setDotsColor(dotColor)
                    .setThickness(4)
                    .setDotsRadius(6)
                    .setDashed(new float[]{10f, 10f});
            holder.measChart.addData(dataset);

            holder.measChart.setBorderSpacing(Tools.fromDpToPx(15))
                    .setStep(1)
                    .setGrid(ChartView.GridType.FULL, 4, 12, gridPaint)
                    .setAxisBorderValues(-20, 1025)
                    .setYLabels(AxisController.LabelPosition.NONE)
                    .setLabelsColor(gridLabelColor)
                    .setXAxis(true)
                    .setYAxis(true);

            holder.measChart.show();

        }
    }

    @Override
    public int getItemCount() {
        return mDayData.size();
    }
}