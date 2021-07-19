package ru.donenergo.journalr.dao;

import ru.donenergo.journalr.models.HouseSegment;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Street;
import ru.donenergo.journalr.models.Transformator;

import java.util.List;

public interface IStreetDAO {

    Integer getStreetRn(Street street);

    List<Street> getStreets();

    List<HouseSegment> getHouseSegment(Podstation podstation, String houseSegmentNum);

    List<HouseSegment> getHouseSegment(Transformator transformator, String houseSegmentNum);

    List<HouseSegment> getHouseSegment(Street street);

    List<HouseSegment> getHouseSegment(Street street, int houseNum);

    void addHouseSegment(HouseSegment houseSegment);

    void deleteHouseSegment(HouseSegment houseSegment);

}
