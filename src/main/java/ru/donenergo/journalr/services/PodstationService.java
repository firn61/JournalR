package ru.donenergo.journalr.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import ru.donenergo.journalr.controllers.CommonController;
import ru.donenergo.journalr.dao.ILineDAO;
import ru.donenergo.journalr.dao.IPodstationDAO;
import ru.donenergo.journalr.dao.ITransformatorDAO;
import ru.donenergo.journalr.models.Line;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Transformator;

import javax.annotation.PostConstruct;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PodstationService implements IPodstationService, IDataToModelSetter {

    private final String NORMAL = "";
    private final String INTERMEDIATE = "_P";
    private final IPodstationDAO podstationDAO;
    private final ITransformatorDAO transformatorDAO;
    private final ILineDAO lineDAO;
    private Podstation currentPodstation;
    private String errorMessage = "";
    static final Logger logger = LoggerFactory.getLogger(PodstationService.class);

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
        logger.info("selected podstation {}", podstation);
        podstation.setTransformators(transformatorDAO.getTransformators(podstation.getRn(), NORMAL));
        for (int i = 0; i < podstation.getTransformators().size(); i++) {
            logger.info("transformator {}", podstation.getTransformators().get(i));
            int transformatorRn = podstation.getTransformators().get(i).getRn();
            podstation.getTransformators().get(i).setLines(lineDAO.getLines(transformatorRn, NORMAL));
        }
        if (transformatorDAO.isTransformatorPExist(podstation.getRn())) {
            podstation.setTransformatorsP(transformatorDAO.getTransformators(podstation.getRn(), INTERMEDIATE));
            for (int i = 0; i < podstation.getTransformatorsP().size(); i++) {
                logger.info("transformatorP {}", podstation.getTransformators().get(i));
                int transformatorRn = podstation.getTransformators().get(i).getRn();
                podstation.getTransformatorsP().get(i).setLines(lineDAO.getLines(transformatorRn, INTERMEDIATE));
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

    public void getPodstationByNumAndType(int num, String podstType, int period) {
        try {
            Integer podstationRn = podstationDAO.getPodstationRn(num, podstType, period);
            currentPodstation = getPodstation(podstationRn);
        } catch (EmptyResultDataAccessException e) {
            errorMessage = "Подстанция " + podstType + "-" + num + " не найдена";
        }
    }

    public void podstationRnCheck(int rn) {
        if (currentPodstation.getRn() != rn) {
            System.out.println("NE");
        }
    }

    public void updatePodstationValues(Podstation savingPodstation) {
        if (savingPodstation.getTransformators().size() > 0) {
            for (int savingTransNum = 0; savingTransNum < savingPodstation.getTransformators().size(); savingTransNum++) {
                updateTransformatorValues(savingPodstation.getTransformators().get(savingTransNum), savingTransNum, NORMAL);
            }
        }
        if (savingPodstation.getTransformatorsP().size() > 0) {
            for (int savingTransNum = 0; savingTransNum < savingPodstation.getTransformatorsP().size(); savingTransNum++) {
                updateTransformatorValues(savingPodstation.getTransformatorsP().get(savingTransNum), savingTransNum, INTERMEDIATE);
            }
        }
    }

    public void updateTransformatorValues(Transformator savingTransformator, int transNum, String additionalPostfix) {
        int sumiA, sumiB, sumiC, sumiO;
        sumiA = sumiB = sumiC = sumiO = 0;
        for (int savingLineNum = 0; savingLineNum < savingTransformator.getLines().size(); savingLineNum++) {
            Line savingLine = savingTransformator.getLines().get(savingLineNum);
            sumiA += savingLine.getiA();
            sumiB += savingLine.getiB();
            sumiC += savingLine.getiC();
            sumiO += savingLine.getiO();
            if (!savingLine.equals(currentPodstation.getTransformators().get(transNum).getLines().get(savingLineNum))) {
                lineDAO.updateLineValues(savingLine, additionalPostfix);
            }
        }
        savingTransformator.setiA(sumiA);
        savingTransformator.setiA(sumiB);
        savingTransformator.setiC(sumiC);
        savingTransformator.setiN(sumiO);
        if (!savingTransformator.equals(currentPodstation.getTransformators().get(transNum))) {
            transformatorDAO.updateTransformatorValues(savingTransformator, additionalPostfix);
        }

    }

    public void updateLineValues(Line savingLine, String additionalPostfix) {

    }

}
