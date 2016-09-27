package com.dev.mieto.homeweatherstation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mieto on 2016-09-25.
 */
public class ConnHwResult {

    private List<ConnHw> hwList = new ArrayList<>();

    public List<ConnHw> getHwList() {
        return hwList;
    }

    public void setHwList(List<ConnHw> hwList) {
        this.hwList = hwList;
    }

}
