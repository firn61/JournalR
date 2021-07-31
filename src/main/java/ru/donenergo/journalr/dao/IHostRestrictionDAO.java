package ru.donenergo.journalr.dao;

import ru.donenergo.journalr.models.Host;

import java.util.List;
import java.util.Map;

public interface IHostRestrictionDAO {

    Integer getHostResNum(String ipAddress);

    String getHostCyrResName(String ipAddress);

    String getHostRights(String ipAddress);

    Map<String, String> getRes();

    Map<String, String> getUsers();

    List<Host> getHosts();

    void addReadOnlyRights(String ipAddress);

    void updateHost(String ipAddress, String rights);

    List<String> getUsrNames();

}
