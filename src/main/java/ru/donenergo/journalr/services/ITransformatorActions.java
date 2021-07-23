package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Transformator;

public interface ITransformatorActions {

    boolean updateTransformatorValues(Transformator savingTransformator, int transNum, String additionalPostfix);

    void addTransformator(Podstation podstation);

    public void deleteTransformator(int rn);

}
