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

    private int maxValueColor;
    private int actValueColor;
    private int backColor;

    private int width;
    private int height;

    private Paint mPaint;

    private RectF shape;

    public DonutGraphView(Context context){
        super(context);
       // init();
    }


    public DonutGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);

//        maxValueColor = attrs.getAttributeIntValue("donut_color", "max_value_color", Color.rgb(0xff,0x00,0xff));
//        actValueColor = attrs.getAttributeIntValue("donut_color", "act_value_color", Color.rgb(0xff,0xff,0x00));
//        backColor = attrs.getAttributeIntValue("donut_color", "back_color", Color.rgb(0x00,0xff,0xff));
//        width = attrs.getAttributeIntValue("width","width",100);
//        height = attrs.getAttributeIntValue("height","height", 100);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.DonutGraphView);
        //Use a
        actValueColor = a.getColor(
                R.styleable.DonutGraphView_act_value_color, Color.GREEN);
        backColor = a.getColor(
                R.styleable.DonutGraphView_back_color, Color.BLACK);
        maxValueColor = a.getColor(
                R.styleable.DonutGraphView_max_value_color, Color.RED);

        width = (int) a.getDimension(R.styleable.DonutGraphView_donut_width, 100);
        height = (int) a.getDimension(R.styleable.DonutGraphView_donut_height, 100);

        mPaint = new Paint();
        mPaint.setColor(backColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(width/10);
        mPaint.setAntiAlias(true);

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*Big oval*/
        mPaint.setColor(backColor);
        canvas.drawArc(shape, 135, 290, false, mPaint);

        mPaint.setColor(maxValueColor);
        canvas.drawArc(shape, 115, 215, false, mPaint);

        mPaint.setColor(actValueColor);
        canvas.drawArc(shape, 115, 150, false, mPaint);
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

    public void setPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }
}
