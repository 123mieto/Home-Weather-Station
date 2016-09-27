package com.dev.mieto.homeweatherstation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mieto on 2016-09-11.
 */
public class DayDataResult {

    /* This variable should be called the same as container in API call */
    private List<DayData> days = new ArrayList<>();

    public List<DayData> getDays(){
        return days;
    }

    public void setDays(ArrayList<DayData> days){
        this.days = days;
    }

}
