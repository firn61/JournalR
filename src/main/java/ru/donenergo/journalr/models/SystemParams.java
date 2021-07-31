package ru.donenergo.journalr.models;

import java.util.List;

public class SystemParams {
    private String filesDir1;
    private String filesDir2;
    private String currentDate;
    private List<Host> hosts;

    public SystemParams(String filesDir1, String filesDir2, String currentDate, List<Host> hosts) {
        this.filesDir1 = filesDir1;
        this.filesDir2 = filesDir2;
        this.currentDate = currentDate;
        this.hosts = hosts;
    }

    public String getFilesDir1() {
        return filesDir1;
    }

    public void setFilesDir1(String filesDir1) {
        this.filesDir1 = filesDir1;
    }

    public void setFilesDir2(String filesDir2) {
        this.filesDir2 = filesDir2;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getFilesDir2() {
        return filesDir2;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }
}
