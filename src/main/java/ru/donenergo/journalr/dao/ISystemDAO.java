package ru.donenergo.journalr.dao;

public interface ISystemDAO {

    String getSystemValue(String sParam);

    void updateSystemValue(String sValue, String sParam);

}
