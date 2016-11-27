package com.dev.mieto.homeweatherstation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mieto on 2016-11-17.
 */
public class DonutGraphView extends View {

    private static final int START_ANGLE = 120;
    private static final int END_ANGLE = 300;

    private int mViewWidth, mViewHeigth;

    private int mMaxValueColor;
    private int mActValueColor;
    private int mBackColor;

    private int mRangeStart;
    private int mRangeEnd;

    private int mActValue;
    private int mMaxValue;

    private int width;
    private int height;

    private int mTextColor, mTextColorDark;
    private String mGraphTitle;
    private String mActCaption;
    private String mMaxCaption;
    private String mValSymbol;

    private Paint mActPaint, mMaxValPaint, mBackPaint, mTextPaint, mTextPaintDark;

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
        try{
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
            mTextColorDark = a.getColor(R.styleable.DonutGraphView_donut_text_color_darker, Color.BLACK);
            mGraphTitle = a.getString(R.styleable.DonutGraphView_donut_title);
            mActCaption = a.getString(R.styleable.DonutGraphView_donut_act_caption);
            mMaxCaption = a.getString(R.styleable.DonutGraphView_donut_max_caption);
            mValSymbol = a.getString(R.styleable.DonutGraphView_donut_value_symbol);
        }finally{
            a.recycle();
        }

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

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(width/10);

        mTextPaintDark = new Paint();
        mTextPaintDark.setColor(mTextColorDark);
        mTextPaintDark.setAntiAlias(true);
        mTextPaintDark.setTextSize(width/8);
        mTextPaintDark.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*Big oval*/
        canvas.drawArc(shape, START_ANGLE, END_ANGLE, false, mBackPaint);

        canvas.drawArc(shape, START_ANGLE, recalculateAngle(mMaxValue), false, mMaxValPaint);

        canvas.drawArc(shape, START_ANGLE, recalculateAngle(mActValue), false, mActPaint);

        canvas.drawText(Integer.toString(mActValue), mViewWidth / 2 - mTextPaint.measureText(Integer.toString(mActValue) + " "),  2 * mViewHeigth / 5, mTextPaintDark);
        canvas.drawText(" " + mValSymbol, mViewWidth / 2 + mTextPaint.measureText(" "), 2 * mViewHeigth / 5, mTextPaint);
        canvas.drawText(mGraphTitle , mViewWidth / 2 -  mTextPaint.measureText(mGraphTitle)/2, mViewHeigth / 2, mTextPaint);
        String str = Integer.toString(mMaxValue) + " " + mValSymbol + " max";
        float width = mTextPaint.measureText(str);

        canvas.drawText(str, mViewWidth / 2 - width / 2, 3 *  mViewHeigth / 5, mTextPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        shape = new RectF((w - width) / 2, (h - height) / 2,
                w - (w - width) / 2, h - (h - height) / 2);
        mViewWidth = w;
        mViewHeigth = h;
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
