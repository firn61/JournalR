package ru.donenergo.journalr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.donenergo.journalr.dao.*;
import ru.donenergo.journalr.models.*;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private final ISystemDAO systemDAO;
    private final IHostRestrictionDAO hostRestrictionDAO;
    private final PodstationDAO podstationDAO;
    private final ICommonDAO commonDAO;
    private final ITransformatorDAO transformatorDAO;
    private final ILineDAO lineDAO;

    public static final String FILES_DIR_1 = "filesdir";
    public static final String FILES_DIR_2 = "filesdir2";
    public static final String CURRENT_DATE = "currentDate";

    @Autowired
    public AdminService(ISystemDAO systemDAO, IHostRestrictionDAO hostRestrictionDAO,
                        PodstationDAO podstationDAO, ICommonDAO commonDAO,
                        ITransformatorDAO transformatorDAO, ILineDAO lineDAO) {
        this.systemDAO = systemDAO;
        this.hostRestrictionDAO = hostRestrictionDAO;
        this.podstationDAO = podstationDAO;
        this.commonDAO = commonDAO;
        this.transformatorDAO = transformatorDAO;
        this.lineDAO = lineDAO;
    }

    public List<Host> getHosts(){
        return hostRestrictionDAO.getHosts();
    }

    public void updateSystemParams(SystemParams systemParams){
        updateSystemValue(systemParams.getCurrentDate(), CURRENT_DATE);
        updateSystemValue(systemParams.getFilesDir1(), FILES_DIR_1);
        updateSystemValue(systemParams.getFilesDir2(), FILES_DIR_2);
    }

    public void updateHosts(SystemParams systemParams){

        List<String> usrNames = hostRestrictionDAO.getUsrNames();
        systemParams.getHosts().stream()
                .filter(host -> usrNames.contains(host.getRights()))
                .forEach(host -> hostRestrictionDAO.updateHost(host.getIp(), host.getRights()));
    }

    public void updateSystemValue(String value, String param) {
        systemDAO.updateSystemValue(value, param);
    }

    public String getSystemValue(String param) {
        return systemDAO.getSystemValue(param);
    }

    public SystemParams getSystemParams() {
        return new SystemParams(getSystemValue(FILES_DIR_1),
                getSystemValue(FILES_DIR_2),
                getSystemValue(CURRENT_DATE),
                getHosts());
    }

    public int startNewPeriod(){
        int currentDate = commonDAO.getCurrentPeriod();
        int targetDate = currentDate + 1;
        List<Podstation> currentPodstations = podstationDAO.getPodstations(currentDate);
        for (Podstation p : currentPodstations){
            int pRn = podstationDAO.addPodstation(p);
            List<Transformator> oldTransformators = transformatorDAO.getTransformators(p.getRn(), "");
            for (Transformator t: oldTransformators) {
                int tRn = transformatorDAO.addTransformatorToNewPeriod(pRn, t);
                List<Line> lines = lineDAO.getLines(t.getRn(), "");
                for (Line l : lines){
                    lineDAO.addLineToNewPeriod(tRn, l);
                }
            }
        }
        return targetDate;
    }

}
