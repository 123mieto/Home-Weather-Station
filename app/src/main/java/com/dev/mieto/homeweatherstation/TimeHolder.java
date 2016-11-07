package com.dev.mieto.homeweatherstation;

/**
 * Created by Mieto on 2016-11-07.
 */
public class TimeHolder {

    private int mTime;
    private StringBuilder mTimeStr;

    private static final int TIME_ADD = 30;

    private void reset() {
        mTime = 0;

    }

    public void increment() {
        if ((mTime / 60) == 24) {
            reset();
            return;
        }
        mTime += TIME_ADD;
    }

    public String toString() {
        mTimeStr = new StringBuilder();
        mTimeStr.append(String.valueOf(mTime / 60));
        mTimeStr.append(":");
        if ((mTime % 60) == 0){
            mTimeStr.append("00");
        }else{
            mTimeStr.append("30");
        }

        return mTimeStr.toString();
    }
}
