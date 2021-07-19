package ru.donenergo.journalr.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapMapper implements ResultSetExtractor<Map> {

    @Override
    public Map extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        HashMap<String, String> extractedMap = new HashMap<>();
        while (resultSet.next()) {
            extractedMap.put(resultSet.getString(1), resultSet.getString(2));
        }
        return extractedMap;
    }
}
