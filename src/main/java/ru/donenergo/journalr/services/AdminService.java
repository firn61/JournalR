package ru.donenergo.journalr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.donenergo.journalr.dao.ISystemDAO;

@Service
public class AdminService {

    private final ISystemDAO systemDAO;
    public static final String FILES_DIR_1 = "filesDir1";
    public static final String FILES_DIR_2 = "filesDir2";
    public static final String CURRENT_DATE = "currentDate";

    @Autowired
    public AdminService(ISystemDAO systemDAO) {
        this.systemDAO = systemDAO;
    }

    public void updateSystemValue(String value, String param){
        systemDAO.updateSystemValue(value, param);
    }
    public String getSystemValue(String param){
        return systemDAO.getSystemValue(param);
    }

}
