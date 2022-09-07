package model;

import java.time.LocalDateTime;

public class EnergyMeter {
    private String  date;
    private int meter;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMeter() {
        return meter;
    }

    public void setMeter(int meter) {
        this.meter = meter;
    }

    public EnergyMeter() {
    }

    public EnergyMeter(String date, int meter) {
        this.date = date;
        this.meter = meter;
    }
}
