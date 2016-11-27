package com.dev.mieto.homeweatherstation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mieto on 2016-11-17.
 */
public class DonutGraphView extends View {

    private static final int START_ANGLE = 120;
    private static final int END_ANGLE = 300;

    private int mMaxValueColor;
    private int mActValueColor;
    private int mBackColor;

    private int mRangeStart;
    private int mRangeEnd;

    private int mActValue;
    private int mMaxValue;

    private int width;
    private int height;

    private int mTextColor;
    private String mGraphTitle;
    private String mActCaption;
    private String mMaxCaption;

    private Paint mActPaint, mMaxValPaint, mBackPaint;

    private RectF shape;

    public DonutGraphView(Context context){
        super(context);
       // init();
    }


    public DonutGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.DonutGraphView);
        //Use a
        mActValueColor = a.getColor(
                R.styleable.DonutGraphView_act_value_color, Color.GREEN);
        mBackColor = a.getColor(
                R.styleable.DonutGraphView_back_color, Color.BLACK);
        mMaxValueColor = a.getColor(
                R.styleable.DonutGraphView_max_value_color, Color.RED);
        width = (int) a.getDimension(R.styleable.DonutGraphView_donut_width, 100);
        height = (int) a.getDimension(R.styleable.DonutGraphView_donut_height, 100);
        mRangeStart = a.getInteger(R.styleable.DonutGraphView_donut_range_start, 0);
        mRangeEnd = a.getInteger(R.styleable.DonutGraphView_donut_range_end, 100);
        mTextColor = a.getColor(R.styleable.DonutGraphView_donut_text_color, Color.BLACK);
        mGraphTitle = a.getString(R.styleable.DonutGraphView_donut_title);
        mActCaption = a.getString(R.styleable.DonutGraphView_donut_act_caption);
        mMaxCaption = a.getString(R.styleable.DonutGraphView_donut_max_caption);

        mBackPaint = new Paint();
        mBackPaint.setColor(mBackColor);
        mBackPaint.setStyle(Paint.Style.STROKE);
        mBackPaint.setStrokeCap(Paint.Cap.ROUND);
        mBackPaint.setStrokeWidth(width/10);
        mBackPaint.setAntiAlias(true);

        mMaxValPaint = new Paint();
        mMaxValPaint.setColor(mMaxValueColor);
        mMaxValPaint.setStyle(Paint.Style.STROKE);
        mMaxValPaint.setStrokeCap(Paint.Cap.ROUND);
        mMaxValPaint.setStrokeWidth(width/10);
        mMaxValPaint.setAntiAlias(true);

        mActPaint = new Paint();
        mActPaint.setColor(mActValueColor);
        mActPaint.setStyle(Paint.Style.STROKE);
        mActPaint.setStrokeCap(Paint.Cap.ROUND);
        mActPaint.setStrokeWidth(width/10);
        mActPaint.setAntiAlias(true);

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*Big oval*/
        canvas.drawArc(shape, START_ANGLE, END_ANGLE, false, mBackPaint);

        canvas.drawArc(shape, START_ANGLE, recalculateAngle(mMaxValue), false, mMaxValPaint);

        canvas.drawArc(shape, START_ANGLE, recalculateAngle(mActValue), false, mActPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        shape = new RectF((w - width) / 2, (h - height) / 2,
                w - (w - width) / 2, h - (h - height) / 2);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setActValue(int mActValue) {
        this.mActValue = mActValue;
        if (mActValue > this.mMaxValue){
            mMaxValue = mActValue;
        }
    }

    public void setMaxValue(int mMaxValue) {
        this.mMaxValue = mMaxValue;
    }

    private int recalculateAngle(int val){
        return (END_ANGLE) * val / (mRangeEnd - mRangeStart);
    }
}
