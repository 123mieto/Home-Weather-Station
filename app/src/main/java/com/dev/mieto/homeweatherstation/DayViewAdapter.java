package com.dev.mieto.homeweatherstation;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mieto on 2016-09-11.
 */
public class DayViewAdapter extends RecyclerView.Adapter<DayViewAdapter.DayViewViewHolder> {

    //TODO: dodac taki opis na dole wykresu
    private final String[] xLabelTime = {"0", "2", "4", "6", "8", "10", "12", "14", "16", "18","20","22","24"};

    private List<DayDataTemp> mDayData = new ArrayList<>();

    private long[] mTimesREST;
    private int[] mTemperaturesREST;

    public DayViewAdapter(List<DayDataTemp> results) {
        /*Show only those results that collected any data*/
        for (DayDataTemp result : results) {
            if (result.getTemperatures().length > 0) {
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

            holder.measChart.setBorderSpacing(Tools.fromDpToPx(0))
                    .setStep(1)
                    .setGrid(ChartView.GridType.FULL, 4, 12, gridPaint)
                    .setAxisBorderValues(-5, 30)
                    .setYLabels(AxisController.LabelPosition.NONE)
                    .setLabelsColor(gridLabelColor)
                    .setAxisColor(gridLabelColor)
                    .setAxisThickness(1)
                    .setXAxis(false)
                    .setYAxis(true);

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
            measChart = (LineChartView) itemView.findViewById(R.id.day_chart);
        }
    }
}
