package ru.donenergo.journalr.dao;

import ru.donenergo.journalr.models.BasicPodstation;
import ru.donenergo.journalr.models.Period;

import java.util.List;

public interface ICommonDAO {

    Integer getCurrentPeriod();

    List<BasicPodstation> getPodstationLabels(int dateRn);

    List<String> getPodstationTypes(int dateRn);

    List<Period> getPeriods(int dateRn);

}
