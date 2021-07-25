package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.Podstation;

public interface IIntermediateActions {

    String createIntermediateMeasure(Podstation podstation);

    String deleteIntermediateTransformator(int rn);

}
