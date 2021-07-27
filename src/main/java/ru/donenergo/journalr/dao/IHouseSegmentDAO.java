package ru.donenergo.journalr.dao;

import ru.donenergo.journalr.models.HouseSegment;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Street;
import ru.donenergo.journalr.models.Transformator;

import java.util.List;

public interface IHouseSegmentDAO {

    List<HouseSegment> getHouseSegment(Podstation podstation);

    List<HouseSegment> getHouseSegment(Podstation podstation, int trNum);

    List<HouseSegment> getHouseSegment(Street street);

    List<HouseSegment> getHouseSegment(Street street, Integer houseNum);

    Integer addHouseSegment(HouseSegment houseSegment);

    void deleteHouseSegment(int rn);

}
