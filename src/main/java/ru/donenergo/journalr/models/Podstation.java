package ru.donenergo.journalr.models;

import java.util.List;

public class Podstation extends BasicPodstation{

    private String numStr;
    private int resNum;
    private int dateRn;
    private int isActive;
    private String address;
    private List<Transformator> transformators;
    private List<Transformator> transformatorsP;

    public Podstation() {
    }

    public Podstation(int rn, int num, String podstType){
        super(rn, num, podstType);
    }

    public String getNumStr() {
        return numStr;
    }

    public void setNumStr(String numStr) {
        this.numStr = numStr;
    }

    public int getResNum() {
        return resNum;
    }

    public void setResNum(int resNum) {
        this.resNum = resNum;
    }

    public int getDateRn() {
        return dateRn;
    }

    public void setDateRn(int dateRn) {
        this.dateRn = dateRn;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Transformator> getTransformators() {
        return transformators;
    }

    public void setTransformators(List<Transformator> transformators) {
        this.transformators = transformators;
    }

    public List<Transformator> getTransformatorsP() {
        return transformatorsP;
    }

    public void setTransformatorsP(List<Transformator> transformatorsP) {
        this.transformatorsP = transformatorsP;
    }
}
