package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.BasicPodstation;
import ru.donenergo.journalr.models.Period;

import java.util.List;

public interface ICommonService {

    List<BasicPodstation> getPodstationLabelsFromDB(int dateRn);

    List<String> getPodstationTypesFromDB(int dateRn);

    List<Period> getPeriodsFromDB(int dateRn);

    Integer getCurrentPeriodFromDB();

}
