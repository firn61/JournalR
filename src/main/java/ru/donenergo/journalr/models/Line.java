package ru.donenergo.journalr.models;

import java.util.Objects;

public class Line {

    private int sectionNum;
    private int rn;
    private int trRn;
    private int num;
    private String name;
    private int iA;
    private int iB;
    private int iC;
    private int iO;
    private String kA;

    public Line() {
    }

    @Override
    public String toString() {
        return "Line{" +
                "sectionNum=" + sectionNum +
                ", rn=" + rn +
                ", trRn=" + trRn +
                ", num=" + num +
                ", name='" + name + '\'' +
                ", iA=" + iA +
                ", iB=" + iB +
                ", iC=" + iC +
                ", iO=" + iO +
                ", kA='" + kA + '\'' +
                '}';
    }

    public int getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(int sectionNum) {
        this.sectionNum = sectionNum;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public int getTrRn() {
        return trRn;
    }

    public void setTrRn(int trRn) {
        this.trRn = trRn;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getiA() {
        return iA;
    }

    public void setiA(int iA) {
        this.iA = iA;
    }

    public int getiB() {
        return iB;
    }

    public void setiB(int iB) {
        this.iB = iB;
    }

    public int getiC() {
        return iC;
    }

    public void setiC(int iC) {
        this.iC = iC;
    }

    public int getiO() {
        return iO;
    }

    public void setiO(int iO) {
        this.iO = iO;
    }

    public String getkA() {
        return kA;
    }

    public void setkA(String kA) {
        this.kA = kA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return sectionNum == line.sectionNum &&
                rn == line.rn &&
                trRn == line.trRn &&
                num == line.num &&
                iA == line.iA &&
                iB == line.iB &&
                iC == line.iC &&
                iO == line.iO &&
                Objects.equals(name, line.name) &&
                Objects.equals(kA, line.kA);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionNum, rn, trRn, num);
    }
}
