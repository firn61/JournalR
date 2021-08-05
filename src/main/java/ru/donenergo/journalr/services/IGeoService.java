package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.Geo;
import ru.donenergo.journalr.models.Podstation;

public interface IGeoService {

    Geo getGeo(Podstation podstation);

}
