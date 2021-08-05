package ru.donenergo.journalr.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.donenergo.journalr.mappers.GeoMapper;
import ru.donenergo.journalr.mappers.PodstationMapper;
import ru.donenergo.journalr.models.Geo;
import ru.donenergo.journalr.models.Podstation;

@Repository
public class GeoDAO implements IGeoDAO {

    private final JdbcTemplate jdbcTemplate;

    public GeoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Geo getGeo(String podstationName) throws EmptyResultDataAccessException {
        String queryTemplate = "SELECT RN, NAME, LAT, LON, ADDRESS, DIST FROM GEO WHERE NAME = ?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{podstationName}, new GeoMapper());
    }

    @Override
    public void updateGeo(Geo geo) {

    }

    @Override
    public void addGeo(Geo geo) {

    }
}
