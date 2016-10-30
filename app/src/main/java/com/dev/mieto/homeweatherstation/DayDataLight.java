package com.dev.mieto.homeweatherstation;

/**
 * Created by Mieto on 2016-10-30.
 */
public class DayDataLight {

    private String number;
    private String date;
    private int[] light;
    private long[] times;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int[] getLight() {
        return light;
    }

    public void setLight(int[] light) {
        this.light = light;
    }

    public long[] getTimes() {
        return times;
    }

    public void setTimes(long[] times) {
        this.times = times;
    }
}
