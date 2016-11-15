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
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mieto on 2016-09-11.
 */
public class DayViewAdapter extends RecyclerView.Adapter<DayViewAdapter.DayViewViewHolder> {

    /* 144 measurement / INCREMENT NUMBER = MEASURES NUMBER */
    private static final int MEASURES_NUMBER = 48;
    private static final int INCREMENT_NUMBER = 3;

    private List<DayDataTemp> mDayData = new ArrayList<>();

    private Paint gridPaint = new Paint();
    private LineChartView mChart;
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
        TimeHolder timeHolder = new TimeHolder();
        long hour, minutes;



        final float[] tempRESTfloat = new float[MEASURES_NUMBER];
        final String[] tempRESTstr = new String[MEASURES_NUMBER];

        mChart = holder.measChart;

        /*If reloading check if one dataset*/
        if(mChart.getData().size() != 0){
            /*removing the remaining data*/
            mChart.getData().clear();
        }

        holder.date.setText(mDayData.get(position).getDate());
        mTimesREST = mDayData.get(position).getTimes();
        mTemperaturesREST = mDayData.get(position).getTemperatures();
        int mTempRestLen = mTemperaturesREST.length;
        if (mTempRestLen > 0) {

            int elem = 0;
            for (int i = 0; i < MEASURES_NUMBER; i++) {
                if (elem * INCREMENT_NUMBER < mTimesREST.length) {
                    hour = (mTimesREST[elem * INCREMENT_NUMBER] / (1000 * 60 * 60)) % 24;
                    minutes = (mTimesREST[elem * INCREMENT_NUMBER] / (1000 * 60)) % 60;
                    int delta = (int) (hour * 2 + ((minutes > 30) ? 1 : 0));
                    if (delta > i) {
                        if (delta >= MEASURES_NUMBER) {
                            break;
                        } else {
                            for (int j = i; j < delta; j++) {
                                tempRESTfloat[j] = (float) 0;

                            }
                            i = delta;
                        }
                    }


                }
                if ((elem * INCREMENT_NUMBER) >= mTemperaturesREST.length) {
                    tempRESTfloat[i] = (float) 0;
                } else {
                    tempRESTfloat[i] = (float) mTemperaturesREST[elem * INCREMENT_NUMBER];
                }

                elem++;
            }

            for (int i = 0; i < MEASURES_NUMBER; i++) {
                if (i % 4 == 0) {
                    tempRESTstr[i] = timeHolder.toString();
                } else {
                    tempRESTstr[i] = "";
                }
                timeHolder.increment();
            }
            //prepare colors
            int dotColor = mChart.getResources().getColor(R.color.accent);
            int gridLabelColor = mChart.getResources().getColor(R.color.secondary_text);

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
            mChart.addData(dataset);

            mChart.setBorderSpacing(Tools.fromDpToPx(0))
                    .setStep(1)
                    //Bug #98
                    //.setGrid(ChartView.GridType.FULL, 3, 12, gridPaint)
                    .setAxisBorderValues(0, 30)
                    .setYLabels(AxisController.LabelPosition.NONE)
                    .setLabelsColor(gridLabelColor)
                    .setAxisColor(gridLabelColor)
                    .setAxisThickness(1)
                    .setXAxis(false)
                    .setYAxis(true);

            Animation anim = new Animation().setDuration(1500).setEasing(new BounceEase()).setStartPoint(-1, 0);
            mChart.show(anim);

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
