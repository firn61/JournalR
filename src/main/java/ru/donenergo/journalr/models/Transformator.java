package ru.donenergo.journalr.models;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Transformator {
    private final String baseFormatterPattern = "dd.MM.yy' 'HH:mm";
    private final String formatterPattern = "yyyy-MM-dd'T'HH:mm";
    private DateTimeFormatter dateTimeFormatterFromBase = DateTimeFormatter.ofPattern(baseFormatterPattern);
    private DateTimeFormatter dateTimeFormatterFromForm = DateTimeFormatter.ofPattern(formatterPattern);
    private int rn;
    private int tpRn;
    private int num;
    private String fider;
    private int power;
    private int uA;
    private int uB;
    private int uC;
    private int iA;
    private int iB;
    private int iC;
    private int iN;
    @DateTimeFormat(pattern=formatterPattern)
    private LocalDateTime dateTime;
    private String monter;
    private List<Line> lines;

    public Transformator() {
    }

    @Override
    public String toString() {
        return "Transformator{" +
                "rn=" + rn +
                ", tpRn=" + tpRn +
                ", num=" + num +
                ", fider='" + fider + '\'' +
                ", power=" + power +
                ", uA=" + uA +
                ", uB=" + uB +
                ", uC=" + uC +
                ", iA=" + iA +
                ", iB=" + iB +
                ", iC=" + iC +
                ", iN=" + iN +
                ", dateTime=" + dateTime +
                ", monter='" + monter + '\'' +
                '}';
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public int getTpRn() {
        return tpRn;
    }

    public void setTpRn(int tpRn) {
        this.tpRn = tpRn;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getFider() {
        return fider;
    }

    public void setFider(String fider) {
        this.fider = fider;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getuA() {
        return uA;
    }

    public void setuA(int uA) {
        this.uA = uA;
    }

    public int getuB() {
        return uB;
    }

    public void setuB(int uB) {
        this.uB = uB;
    }

    public int getuC() {
        return uC;
    }

    public void setuC(int uC) {
        this.uC = uC;
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

    public int getiN() {
        return iN;
    }

    public void setiN(int iN) {
        this.iN = iN;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

        public void setDateTime(String dateTime) {
        if ((dateTime == null) || (dateTime.length() == 0)) {
            this.dateTime = LocalDateTime.now();
        } else {
            try {
                this.dateTime = LocalDateTime.parse(dateTime, dateTimeFormatterFromForm);
            } catch (DateTimeException e) {
                this.dateTime = LocalDateTime.now();
            }
        }
    }

    public void setDateTimeFromDAO(String dateTime) {
        if ((dateTime == null) || (dateTime.length() == 0)) {
            this.dateTime = null;
        } else {
            try {
                this.dateTime = LocalDateTime.parse(dateTime, dateTimeFormatterFromBase);
            } catch (DateTimeException e) {
                this.dateTime = null;
            }
        }
    }

    public String getDateTimeToDAO(){
        if(dateTime == null) {
            dateTime = LocalDateTime.now();
        }
        String day = String.valueOf(dateTime.getDayOfMonth());
        if (day.length() == 1) {
            day = "0" + day;
        }
        String month = String.valueOf(dateTime.getMonthValue());
        if (month.length() == 1) {
            month = "0" + month;
        }
        String hour = String.valueOf(dateTime.getHour());
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        String minute = String.valueOf(dateTime.getMinute());
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        return day + "." + month + "." + String.valueOf(dateTime.getYear()).substring(2) + " " + hour + ":" + minute;
    }

    public String getMonter() {
        return monter;
    }

    public void setMonter(String monter) {
        this.monter = monter;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transformator that = (Transformator) o;
        return rn == that.rn &&
                tpRn == that.tpRn &&
                num == that.num &&
                power == that.power &&
                uA == that.uA &&
                uB == that.uB &&
                uC == that.uC &&
                iA == that.iA &&
                iB == that.iB &&
                iC == that.iC &&
                iN == that.iN &&
                Objects.equals(fider, that.fider) &&
                Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(monter, that.monter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rn, tpRn, num);
    }
}
