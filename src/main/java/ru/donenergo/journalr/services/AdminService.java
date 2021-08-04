package ru.donenergo.journalr.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.donenergo.journalr.dao.*;
import ru.donenergo.journalr.models.*;

import java.util.List;

@Service
public class AdminService implements IAdminService{

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

    static final Logger logger = LoggerFactory.getLogger(AdminService.class);

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

    @Override
    public List<Host> getHosts(){
        return hostRestrictionDAO.getHosts();
    }

    @Override
    public void updateSystemParams(SystemParams systemParams){
        updateSystemValue(systemParams.getCurrentDate(), CURRENT_DATE);
        updateSystemValue(systemParams.getFilesDir1(), FILES_DIR_1);
        updateSystemValue(systemParams.getFilesDir2(), FILES_DIR_2);
    }

    @Override
    public void updateHosts(SystemParams systemParams){
        List<String> usrNames = hostRestrictionDAO.getUsrNames();
        systemParams.getHosts().stream()
                .filter(host -> usrNames.contains(host.getRights()))
                .forEach(host -> hostRestrictionDAO.updateHost(host.getIp(), host.getRights()));
    }

    @Override
    public void updateSystemValue(String value, String param) {
        systemDAO.updateSystemValue(value, param);
    }

    @Override
    public String getSystemValue(String param) {
        return systemDAO.getSystemValue(param);
    }

    @Override
    public SystemParams getSystemParams() {
        return new SystemParams(getSystemValue(FILES_DIR_1),
                getSystemValue(FILES_DIR_2),
                getSystemValue(CURRENT_DATE),
                getHosts());
    }

    @Override
    public void startNewPeriod(){
        int currentDate = commonDAO.getCurrentPeriod();
        logger.info("currentDate: {}", currentDate);
        int targetDate = currentDate + 1;
        logger.info("targetDate: {}", targetDate);
        List<Podstation> currentPodstations = podstationDAO.getPodstations(currentDate);
        logger.info("currentPodstations size: {}", currentPodstations.size());
        for (Podstation p : currentPodstations){
            p.setDateRn(targetDate);
            logger.info("copyin: {}", p);
            int pRn = podstationDAO.addPodstation(p);
            logger.info("podstation new rn: {}", pRn);
            List<Transformator> oldTransformators = transformatorDAO.getTransformators(p.getRn(), "");
            logger.info("transformators size: {}", oldTransformators.size());
            for (Transformator t: oldTransformators) {
                int tRn = transformatorDAO.addTransformatorToNewPeriod(pRn, t);
                logger.info("transformator: {} with rn: {} added", t, tRn);
                List<Line> lines = lineDAO.getLines(t.getRn(), "");
                logger.info("lines size: {}", lines.size());
                for (Line l : lines){
                    lineDAO.addLineToNewPeriod(tRn, l);
                    logger.info("line: {} added", l);
                }
            }
        }
        updateSystemValue(String.valueOf(targetDate), CURRENT_DATE);
        logger.info("currentDate changed to: {}", targetDate);
    }

}
