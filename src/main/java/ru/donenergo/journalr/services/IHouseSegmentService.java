package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.HouseSegment;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Street;

import java.util.List;

public interface IHouseSegmentService {

    List<HouseSegment> getHouseSegment(Podstation podstation);

    List<HouseSegment> getHouseSegment(Podstation podstation, int trNum);

    List<HouseSegment> getHouseSegment(Street street);

    List<HouseSegment> getHouseSegment(Street street, Integer houseNum);

    Street getStreet(String streetName);

    String addHouseSegment(HouseSegment houseSegment);

    String deleteHouseSegment(int rn);

}
