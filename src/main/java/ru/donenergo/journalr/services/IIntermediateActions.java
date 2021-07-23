package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.Podstation;

public interface IIntermediateActions {

    void createIntermediateMeasure(Podstation podstation);

    void deleteIntermediateTransformator(int rn);

}
