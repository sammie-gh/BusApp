package com.gh.sammie.busapp.EventBus;


import com.gh.sammie.busapp.model.Bus;
import com.gh.sammie.busapp.model.Station;

public class EnableNextButton {
    private int step;
    private Bus bus;
    private Station station;
    private int timeSlot;

    public EnableNextButton(int step, int timeSlot) {
        this.step = step;
        this.timeSlot = timeSlot;
    }

    public EnableNextButton(int step, Bus bus) {
        this.step = step;
        this.bus = bus;
    }

    public EnableNextButton(int step, Station station) {
        this.step = step;
        this.station = station;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }


}
