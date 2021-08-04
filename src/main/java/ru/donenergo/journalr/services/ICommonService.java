package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.BasicPodstation;
import ru.donenergo.journalr.models.Period;

import java.util.List;

public interface ICommonService {

    void refreshCommonValues(int currentPeriod);

    void addMessage(String message);

    void addError(String error);

    void addBasicPodstatinLabel(BasicPodstation newBasicPodstation);

    void changeViewIfIndex();

    String getViewName(int rn);

    List<BasicPodstation> getPodstationLabelsFromDB(int dateRn);

    List<String> getPodstationTypesFromDB(int dateRn);

    List<Period> getPeriodsFromDB(int dateRn);

    Integer getCurrentPeriodFromDB();

}
