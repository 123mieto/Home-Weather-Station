package com.dev.mieto.homeweatherstation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mieto on 2016-09-06.
 */
public class MeasurementResult {

    private List<Measurement> readings = new ArrayList<>();

    public List<Measurement> getReadings() {
        return readings;
    }

    public void setReadings(List<Measurement> readings) {
        this.readings = readings;
    }
}
