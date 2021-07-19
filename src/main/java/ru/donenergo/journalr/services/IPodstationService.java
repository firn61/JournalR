package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.Podstation;

public interface IPodstationService {

    void updatePodstationByRn(int rn);

    void getCurrentPodstationFromNewPeriod(int period);

    Podstation getPodstation(int rn);
}
