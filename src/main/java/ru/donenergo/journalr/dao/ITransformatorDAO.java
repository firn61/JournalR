package ru.donenergo.journalr.dao;

import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Transformator;

import java.util.List;

public interface ITransformatorDAO {

    Transformator getTransformator(int rn, String additionalPostfix);

    Transformator getTransformator(int tpRn, int num, String additionalPostfix);

    List<Transformator> getTransformators(int tpRn, String additionalPostfix);

    Integer addTransformator(Transformator transformator, String additionalPostfix);

    Integer addIntermediateTransformator(Transformator transformator);

    Integer addTransformatorToNewPeriod(int pRn, Transformator transformator);

    void updateTransformatorValues(Transformator transformator, String additionalPostfix);

    void updateTransformatorParams(Transformator transformator);

    void deleteTransformator(Transformator transformator, String additionalPostfix);

    void deleteTransformator(int rn, String additionalPostfix);

    boolean isTransformatorPExist(int rn);

}
