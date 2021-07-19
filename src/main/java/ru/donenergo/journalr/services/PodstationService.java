package ru.donenergo.journalr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import ru.donenergo.journalr.dao.ILineDAO;
import ru.donenergo.journalr.dao.IPodstationDAO;
import ru.donenergo.journalr.dao.ITransformatorDAO;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Transformator;

import javax.annotation.PostConstruct;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PodstationService implements IPodstationService, IDataToModelSetter {

    private final IPodstationDAO podstationDAO;
    private final ITransformatorDAO transformatorDAO;
    private final ILineDAO lineDAO;
    private Podstation currentPodstation;
    private final String normal = "";
    private final String intermediate = "_P";
    private String errorMessage = "";

    @PostConstruct
    private void invokeValues() {
        currentPodstation = new Podstation(0, 0, "");
    }

    @Autowired
    public PodstationService(IPodstationDAO podstationDAO, ITransformatorDAO transformatorDAO, ILineDAO lineDAO) {
        this.podstationDAO = podstationDAO;
        this.transformatorDAO = transformatorDAO;
        this.lineDAO = lineDAO;
    }

    @Override
    public Podstation getPodstation(int rn) {
        Podstation podstation = podstationDAO.getPodstation(rn);
        podstation.setTransformators(transformatorDAO.getTransformators(podstation.getRn(), normal));
        for (int i = 0; i < podstation.getTransformators().size(); i++) {
            int transformatorRn = podstation.getTransformators().get(i).getRn();
            podstation.getTransformators().get(i).setLines(lineDAO.getLines(transformatorRn, normal));
        }
        if (transformatorDAO.isTransformatorPExist(podstation.getRn())) {
            podstation.setTransformatorsP(transformatorDAO.getTransformators(podstation.getRn(), intermediate));
            for (int i = 0; i < podstation.getTransformatorsP().size(); i++) {
                int transformatorRn = podstation.getTransformators().get(i).getRn();
                podstation.getTransformatorsP().get(i).setLines(lineDAO.getLines(transformatorRn, intermediate));
            }
        }
        return podstation;
    }

    public int getCurrentPodstationRn() {
        return currentPodstation.getRn();
    }

    @Override
    public void updatePodstationByRn(int rn) {
        if (currentPodstation.getRn() != rn) {
            currentPodstation = getPodstation(rn);
        }
    }

    @Override
    public void setDataToModel(Model model) {
        model.addAttribute("errorMessage", errorMessage);
        errorMessage = "";
        model.addAttribute("currentPodstation", currentPodstation);

    }

    @Override
    public void getCurrentPodstationFromNewPeriod(int period) {
        try {
            Integer podstationRn = podstationDAO.getPodstationRn(currentPodstation.getNum(), currentPodstation.getPodstType(), period);
            currentPodstation = getPodstation(podstationRn);
        } catch (EmptyResultDataAccessException e) {
            errorMessage = "Подстанция " + currentPodstation.getPodstType() + "-" + currentPodstation.getNum() + " не найдена в выбраном периоде";
        }

    }

    public void getPodstationByNumAndType(int num, String podstType, int period){
        try {
            Integer podstationRn = podstationDAO.getPodstationRn(num, podstType, period);
            currentPodstation = getPodstation(podstationRn);
        } catch (EmptyResultDataAccessException e) {
            errorMessage = "Подстанция " + podstType + "-" + num + " не найдена";
        }

    }


}
