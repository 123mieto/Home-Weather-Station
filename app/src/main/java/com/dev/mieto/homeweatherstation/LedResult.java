package com.dev.mieto.homeweatherstation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mieto on 2016-09-26.
 */
public class LedResult {

    @SerializedName("state")
    private int state;

    @SerializedName("number")
    private int number;

    public int getLedStatus() {
        return state;
    }

    public void setLedStatus(int state) {
        this.state = state;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
