package ru.donenergo.journalr.dao;

import ru.donenergo.journalr.models.Line;
import ru.donenergo.journalr.models.Transformator;

import java.util.List;

public interface ILineDAO {

    Line getLine(int rn, String additionalPostfix);

    Line getLine(int trRn, int num, String additionalPostfix);

    List<Line> getLines(int trRn, String additionalPostfix);

    Integer addLine(Line line, String additionalPostfix);

    Integer addLineToNewPeriod(Line line);

    void updateLineValues(Line line, String additionalPostfix);

    void updateLineParams(Line line);

    void deleteLine(Line line, String additionalPostfix);

    void deleteLines(Transformator transformator, String additionalPostfix);
}
