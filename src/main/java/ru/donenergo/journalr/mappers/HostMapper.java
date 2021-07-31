package ru.donenergo.journalr.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.donenergo.journalr.models.Host;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HostMapper implements RowMapper<Host> {

    @Override
    public Host mapRow(ResultSet resultSet, int i) throws SQLException {
        Host host = new Host();
        host.setIp(resultSet.getString("IP"));
        host.setRights(resultSet.getString("RIGHTS"));
        return host;
    }
}
