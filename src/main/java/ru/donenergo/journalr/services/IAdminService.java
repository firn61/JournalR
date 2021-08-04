package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.Host;
import ru.donenergo.journalr.models.SystemParams;

import java.util.List;

public interface IAdminService {

    List<Host> getHosts();

    void updateSystemParams(SystemParams systemParams);

    void updateHosts(SystemParams systemParams);

    void updateSystemValue(String value, String param);

    String getSystemValue(String param);

    SystemParams getSystemParams();

    void startNewPeriod();

}
