package ru.donenergo.journalr.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.donenergo.journalr.dao.IGeoDAO;
import ru.donenergo.journalr.models.Geo;
import ru.donenergo.journalr.models.Podstation;

@Service
public class GeoService implements IGeoService {

    private final IGeoDAO geoDAO;

    static final Logger logger = LoggerFactory.getLogger(GeoService.class);

    public GeoService(IGeoDAO geoDAO) {
        this.geoDAO = geoDAO;
    }

    @Override
    public Geo getGeo(Podstation podstation) {
        String podstationName = podstation.getDispName();
        try {
            Geo geo = geoDAO.getGeo(podstationName);
            logger.info("current geo: {}", geo);
            return geo;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }
}
