package com.dev.mieto.homeweatherstation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mieto on 2016-09-11.
 */
public class DayDataTempResult {

    /* This variable should be called the same as container in API call */
    private List<DayDataTemp> days = new ArrayList<>();

    public List<DayDataTemp> getDays(){
        return days;
    }

    public void setDays(ArrayList<DayDataTemp> days){
        this.days = days;
    }

}
