package ru.donenergo.journalr.models;

public class Period {

    private int rn;
    private String sDate;

    public Period() {
    }

    @Override
    public String toString() {
        return sDate;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }
}
