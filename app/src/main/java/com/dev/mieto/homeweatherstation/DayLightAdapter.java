package com.dev.mieto.homeweatherstation;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mieto on 2016-10-30.
 */
public class DayLightAdapter extends RecyclerView.Adapter<DayViewAdapter.DayViewViewHolder> {

    /* 144 measurement / INCREMENT NUMBER = MEASURES NUMBER */
    private static final int MEASURES_NUMBER = 48;

    private static final int INCREMENT_NUMBER = 3;

    private List<DayDataLight> mDayData = new ArrayList<>();

    private Paint gridPaint = new Paint();


    public DayLightAdapter(List<DayDataLight> results) {
        /*Show only those results that collected any data*/
        for (DayDataLight result : results) {
            if (result.getLight().length > 0) {
                this.mDayData.add(result);
            }
        }
    }

    private LineSet prepChartDataset() {
        return new LineSet(new String[]{"", ""}, new float[]{0f, 20.0f});
    }

    @Override
    public DayViewAdapter.DayViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new DayViewAdapter.DayViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayViewAdapter.DayViewViewHolder holder, int position) {
        TimeHolder timeHolder = new TimeHolder();
        long hour,minutes;

        LineChartView mChart = holder.measChart;

        /*If reloading check if one dataset*/
        if(mChart.getData().size() != 0){
            /*removing the remaining data*/
            mChart.getData().clear();
        }

        holder.date.setText(mDayData.get(position).getDate());
        long[] mTimesREST = mDayData.get(position).getTimes();
        int[] mLightsREST = mDayData.get(position).getLight();
        int mTempRestLen = mLightsREST.length;
        if (mTempRestLen > 0) {
            float[] tempRESTfloat = new float[MEASURES_NUMBER];
            String[] tempRESTstr = new String[MEASURES_NUMBER];

            int elem = 0;
            for (int i = 0; i < MEASURES_NUMBER; i++) {
                if (elem * INCREMENT_NUMBER < mTimesREST.length) {
                    hour = (mTimesREST[elem * INCREMENT_NUMBER] / (1000 * 60 * 60)) % 24;
                    minutes = (mTimesREST[elem * INCREMENT_NUMBER] / (1000 * 60)) % 60;
                    int delta = (int) (hour * 2 + ((minutes > 30) ? 1 : 0));
                    if (delta > i){
                        if (delta >= MEASURES_NUMBER) {
                            break;
                        }else{
                            for(int j = i; j < delta; j++){
                                tempRESTfloat[j] = (float) 0;

                            }
                            i = delta;
                        }
                    }
                }
                if ((elem * INCREMENT_NUMBER) >= mLightsREST.length){
                    tempRESTfloat[i] = (float) 0;
                }else{
                    tempRESTfloat[i] = (float) mLightsREST[elem * INCREMENT_NUMBER];
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
                    //Bug #98
                    //.setGrid(ChartView.GridType.FULL, 4, 12, gridPaint)
                    .setAxisBorderValues(-20, 1025)
                    .setYLabels(AxisController.LabelPosition.NONE)
                    .setLabelsColor(gridLabelColor)
                    .setAxisColor(gridLabelColor)
                    .setAxisThickness(1)
                    .setXAxis(true)
                    .setYAxis(false);

            Animation anim = new Animation().setDuration(1500).setEasing(new BounceEase()).setStartPoint(-1, 0);
            mChart.show(anim);

        }
    }

    @Override
    public int getItemCount() {
        return mDayData.size();
    }


}
