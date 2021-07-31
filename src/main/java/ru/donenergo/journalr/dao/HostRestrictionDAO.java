package ru.donenergo.journalr.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.donenergo.journalr.mappers.HostMapper;
import ru.donenergo.journalr.mappers.MapMapper;
import ru.donenergo.journalr.models.Host;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class HostRestrictionDAO implements IHostRestrictionDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HostRestrictionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getHostResNum(String ipAddress) {
        final String queryTemplate = "SELECT USR_TABLE.RES_NUM FROM USR_TABLE, HOSTS WHERE USR_TABLE.USR_NAME = HOSTS.RIGHTS AND HOSTS.IP = ?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{ipAddress}, Integer.class);
    }

    @Override
    public String getHostCyrResName(String ipAddress) {
        final String queryTemplate = "SELECT RES.NAME FROM RES, USR_TABLE, HOSTS WHERE RES.RN = USR_TABLE.RN AND USR_TABLE.USR_NAME = HOSTS.RIGHTS AND HOSTS.IP = ?";
        System.out.println(ipAddress);
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{ipAddress}, String.class);
    }

    @Override
    public String getHostRights(String ipAddress) throws EmptyResultDataAccessException {
        final String queryTemplate = "SELECT RIGHTS FROM HOSTS WHERE IP=?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{ipAddress}, String.class);
    }

    @Override
    public Map<String, String> getRes() {
        final String queryTemplate = "SELECT NUM, NAME FROM RES";
        return jdbcTemplate.query(queryTemplate, new MapMapper());
    }

    @Override
    public Map<String, String> getUsers() {
        final String queryTemplate = "SELECT RES_NUM, USR_NAME FROM USR_TABLE";
        return jdbcTemplate.query(queryTemplate, new MapMapper());
    }

    @Override
    public List<Host> getHosts() {
        final String queryTemplate = "SELECT IP, RIGHTS FROM HOSTS ORDER BY IP";
        return jdbcTemplate.query(queryTemplate, new HostMapper());
    }

    @Override
    public void addReadOnlyRights(String ipAddress) {
        final String queryTemplate = "INSERT INTO HOSTS VALUES(?, ?)";
        jdbcTemplate.update(queryTemplate, new Object[]{ipAddress, "rges0"});
    }

    @Override
    public void updateHost(String ipAddress, String rights) {
        final String queryTemplate = "UPDATE HOSTS SET RIGHTS = ? WHERE IP = ?";
        jdbcTemplate.update(queryTemplate, new Object[]{rights, ipAddress});
    }

    public List<String> getUsrNames() {
        final String queryTemplate = "SELECT USR_NAME FROM USR_TABLE";
        return jdbcTemplate.query(queryTemplate, (rs, rowNum) -> rs.getString(1));
    }

}
