package ru.donenergo.journalr.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.donenergo.journalr.models.BasicPodstation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BasicPodstationMapper implements RowMapper<BasicPodstation> {
    @Override
    public BasicPodstation mapRow(ResultSet resultSet, int i) throws SQLException {
        BasicPodstation basicPodstation = new BasicPodstation();
        basicPodstation.setRn(resultSet.getInt("RN"));
        basicPodstation.setPodstType(resultSet.getString("PODST_TYPE"));
        basicPodstation.setNum(resultSet.getInt("NUM"));

        return basicPodstation;
    }
}
