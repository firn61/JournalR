package ru.donenergo.journalr.models;

public class BasicPodstation {

    protected int rn;
    protected int num;
    protected String podstType;

    public BasicPodstation() {
    }

    public BasicPodstation(int rn, int num, String podstType) {
        this.rn = rn;
        this.num = num;
        this.podstType = podstType;
    }

    @Override
    public String toString() {
        return podstType + "-" + num;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public String getPodstType() {
        return podstType;
    }

    public void setPodstType(String podstType) {
        this.podstType = podstType;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
