package ru.donenergo.journalr.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.donenergo.journalr.models.Geo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GeoMapper implements RowMapper<Geo> {

    @Override
    public Geo mapRow(ResultSet resultSet, int i) throws SQLException {
        Geo geo = new Geo();
        geo.setRn(resultSet.getInt("RN"));
        geo.setName(resultSet.getString("NAME"));
        geo.setLat(resultSet.getString("LAT"));
        geo.setLon(resultSet.getString("LON"));
        geo.setAddress(resultSet.getString("ADDRESS"));
        geo.setDist(resultSet.getString("DIST"));
        return geo;
    }
}
