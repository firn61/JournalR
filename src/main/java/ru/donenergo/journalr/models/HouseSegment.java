package ru.donenergo.journalr.models;

public class HouseSegment {

    private int rn;
    private String strPodstation;
    private int trNum;
    private String fider;
    private int streetRn;
    private String streetName;
    private String streetType;
    private String house1;
    private String house1Building;
    private String house2;
    private String house2Building;

    public HouseSegment() {
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public String getStrPodstation() {
        return strPodstation;
    }

    public void setStrPodstation(String strPodstation) {
        this.strPodstation = strPodstation;
    }

    public int getTrNum() {
        return trNum;
    }

    public void setTrNum(int trNum) {
        this.trNum = trNum;
    }

    public String getFider() {
        return fider;
    }

    public void setFider(String fider) {
        this.fider = fider;
    }

    public int getStreetRn() {
        return streetRn;
    }

    public void setStreetRn(int streetRn) {
        this.streetRn = streetRn;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetType() {
        return streetType;
    }

    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    public String getHouse1() {
        return house1;
    }

    public void setHouse1(String house1) {
        this.house1 = house1;
    }

    public String getHouse1Building() {
        return house1Building;
    }

    public void setHouse1Building(String house1Building) {
        this.house1Building = house1Building;
    }

    public String getHouse2() {
        return house2;
    }

    public void setHouse2(String house2) {
        this.house2 = house2;
    }

    public String getHouse2Building() {
        return house2Building;
    }

    public void setHouse2Building(String house2Building) {
        this.house2Building = house2Building;
    }

    @Override
    public String toString() {
        return "HouseSegment{" +
                "rn=" + rn +
                ", strPodstation='" + strPodstation + '\'' +
                ", trNum=" + trNum +
                ", fider='" + fider + '\'' +
                ", streetRn=" + streetRn +
                ", streetName='" + streetName + '\'' +
                ", streetType='" + streetType + '\'' +
                ", house1=" + house1 +
                ", house1Building='" + house1Building + '\'' +
                ", house2=" + house2 +
                ", house2Building='" + house2Building + '\'' +
                '}';
    }
}
