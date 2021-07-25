package ru.donenergo.journalr.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.donenergo.journalr.mappers.MapMapper;

import java.util.Map;

@Repository
public class HostRestrictionDAO implements IHostRestrictionDAO {

    private final JdbcTemplate jdbcTemplate;

    public HostRestrictionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getHostResNum(String ipAddress) {
        String queryTemplate = "SELECT USR_TABLE.RES_NUM FROM USR_TABLE, HOSTS WHERE USR_TABLE.USR_NAME = HOSTS.RIGHTS AND HOSTS.IP = ?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{ipAddress}, Integer.class);
    }

    @Override
    public String getHostCyrResName(String ipAddress) {
        String queryTemplate = "SELECT RES.NAME FROM RES, USR_TABLE, HOSTS WHERE RES.RN = USR_TABLE.RN AND USR_TABLE.USR_NAME = HOSTS.RIGHTS AND HOSTS.IP = ?";
        System.out.println(ipAddress);
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{ipAddress}, String.class);
    }

    @Override
    public String getHostRights(String ipAddress) throws EmptyResultDataAccessException {
        String queryTemplate = "SELECT RIGHTS FROM HOSTS WHERE IP=?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{ipAddress}, String.class);
    }

    @Override
    public Map<String, String> getRes() {
        String queryTemplate = "SELECT NUM, NAME FROM RES";
        return jdbcTemplate.query(queryTemplate, new MapMapper());
    }

    @Override
    public Map<String, String> getUsers() {
        String queryTemplate = "SELECT RES_NUM, USR_NAME FROM USR_TABLE";
        return jdbcTemplate.query(queryTemplate, new MapMapper());
    }

    @Override
    public Map<String, String> getHosts() {
        String queryTemplate = "SELECT IP, RIGHTS FROM HOSTS ORDER BY IP";
        return jdbcTemplate.query(queryTemplate, new MapMapper());
    }

    @Override
    public void addReadOnlyRights(String ipAddress) {
        String queryTemplate = "INSERT INTO HOSTS VALUES(?, ?)";
        jdbcTemplate.update(queryTemplate, new Object[]{ipAddress, "rges0"});
    }

    @Override
    public void updateHost(String ipAddress, String rights) {
        String queryTemplate = "UPDATE HOSTS SET RIGHTS = ? WHERE IP = ?";
        jdbcTemplate.update(queryTemplate, new Object[]{rights, ipAddress});
    }
}
