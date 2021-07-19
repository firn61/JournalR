package ru.donenergo.journalr.dao;

import ru.donenergo.journalr.models.Line;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Transformator;

public interface IPodstationDAO {

    Podstation getPodstation(int rn);

    Integer getPodstationRn(int num, String type, int dateRn);

    Integer podstationCount(Podstation podstation);

    Integer addPodstation(Podstation podstation);

    void updatePodstationParams(Podstation podstation);

}
