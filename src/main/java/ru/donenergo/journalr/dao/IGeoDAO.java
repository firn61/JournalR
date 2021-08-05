package ru.donenergo.journalr.dao;

import ru.donenergo.journalr.models.Geo;
import ru.donenergo.journalr.models.Podstation;

public interface IGeoDAO {

    Geo getGeo(String podstationName);

    void updateGeo(Geo geo);

    void addGeo(Geo geo);

}
