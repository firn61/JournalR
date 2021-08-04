package ru.donenergo.journalr.dao;

import ru.donenergo.journalr.models.HouseSegment;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Street;
import ru.donenergo.journalr.models.Transformator;

import java.util.List;

public interface IStreetDAO {

    Integer getStreetRn(Street street);

    List<Street> getStreets();

    Street getStreet(String streetName, String streetType);

}
