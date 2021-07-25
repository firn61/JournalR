package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Transformator;

public interface IPodstationActions {

    void updatePodstationByRn(int rn);

    String getCurrentPodstationFromNewPeriod(int period);

    Podstation getPodstation(int rn);

    String getPodstationByNumAndType(int num, String podstType, int period);

    void podstationRnCheck(int rn);

    String updatePodstationValues(Podstation savingPodstation);

    String updatePodstationParams(Podstation savingPodstation);

}
