package ru.donenergo.journalr.dao;

import java.util.Map;

public interface IHostRestrictionDAO {

    Integer getHostResNum(String ipAddress);

    String getHostCyrResName(String ipAddress);

    String getHostRights(String ipAddress);

    Map<String, String> getRes();

    Map<String, String> getUsers();

    Map<String, String> getHosts();

    void addReadOnlyRights(String ipAddress);

    void updateHost(String ipAddress, String rights);

}
