package ru.donenergo.journalr.services;

public interface ILineActions {

    String addLine(int transformatorRn);

    String moveLine(int rn, String direction);

    String deleteLine(int rn);

}
