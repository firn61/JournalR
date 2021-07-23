package ru.donenergo.journalr.services;

public interface ILineActions {

    void addLine(int transformatorRn);

    public void moveLine(int rn, String direction);

    void deleteLine(int rn);

}
