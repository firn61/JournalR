package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Transformator;

public interface ITransformatorActions {

    boolean updateTransformatorValues(Transformator savingTransformator, int transNum, String additionalPostfix);

    String addTransformator(Podstation podstation);

    String deleteTransformator(int rn);

}
