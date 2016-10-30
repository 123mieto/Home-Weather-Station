package com.dev.mieto.homeweatherstation;

/**
 * Created by Mieto on 2016-09-11.
 */
public class DayDataTemp {

    /*Those variables should correspond to data returned by REST API */
    private String number;
    private String date;
    private int[] temperatures;
    private long[] times;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int[] getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(int[] temperatures) {
        this.temperatures = temperatures;
    }

    public long[] getTimes() {
        return times;
    }

    public void setTimes(long[] times) {
        this.times = times;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
