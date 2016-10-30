package com.dev.mieto.homeweatherstation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mieto on 2016-10-30.
 */
public class DayDataLightResult {

    private List<DayDataLight> days = new ArrayList<>();


    public List<DayDataLight> getDays() {
        return days;
    }

    public void setDays(List<DayDataLight> days) {
        this.days = days;
    }
}
