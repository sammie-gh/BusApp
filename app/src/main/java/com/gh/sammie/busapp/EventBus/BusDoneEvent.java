package com.gh.sammie.busapp.EventBus;


import com.gh.sammie.busapp.model.Bus;

import java.util.List;

public class BusDoneEvent {
    private List<Bus> barberList;

    public BusDoneEvent(List<Bus> barberList) {
        this.barberList = barberList;
    }

    public List<Bus> getBusList() {
        return barberList;
    }

    public void setBarberList(List<Bus> barberList) {
        this.barberList = barberList;
    }
}
