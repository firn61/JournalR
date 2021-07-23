package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Transformator;

public interface IPodstationActions {

    void updatePodstationByRn(int rn);

    void getCurrentPodstationFromNewPeriod(int period);

    Podstation getPodstation(int rn);

    void getPodstationByNumAndType(int num, String podstType, int period);

    void podstationRnCheck(int rn);

    boolean updatePodstationValues(Podstation savingPodstation);

    boolean updatePodstationParams(Podstation savingPodstation);

}
