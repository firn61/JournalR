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
import ru.donenergo.journalr.dao.ILineDAO;
import ru.donenergo.journalr.dao.IPodstationDAO;
import ru.donenergo.journalr.dao.ITransformatorDAO;
import ru.donenergo.journalr.models.Line;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Transformator;

import javax.annotation.PostConstruct;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PodstationService implements IPodstationActions, ITransformatorActions, ILineActions, IIntermediateActions, IDataToModelSetter {

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

    public void refreshCurrentPodstation() {
        currentPodstation = getPodstation(currentPodstation.getRn());
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
                logger.info("transformatorP {}", podstation.getTransformatorsP().get(i));
                int transformatorRn = podstation.getTransformatorsP().get(i).getRn();
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

    @Override
    public void getPodstationByNumAndType(int num, String podstType, int period) {
        try {
            Integer podstationRn = podstationDAO.getPodstationRn(num, podstType, period);
            currentPodstation = getPodstation(podstationRn);
        } catch (EmptyResultDataAccessException e) {
            errorMessage = "Подстанция " + podstType + "-" + num + " не найдена";
        }
    }

    @Override
    public void podstationRnCheck(int rn) {
        if (currentPodstation.getRn() != rn) {
            System.out.println("NE");
        }
    }

    @Override
    public boolean updatePodstationValues(Podstation savingPodstation) {
        boolean updated = false;
        boolean updatedP = false;
        if ((savingPodstation.getTransformators() != null) && (savingPodstation.getTransformators().size() > 0)) {
            for (int savingTransNum = 0; savingTransNum < savingPodstation.getTransformators().size(); savingTransNum++) {
                logger.info("processing trans {} ", savingPodstation.getTransformators().get(savingTransNum));
                updated = updated | updateTransformatorValues(savingPodstation.getTransformators().get(savingTransNum), savingTransNum, NORMAL);
            }
        }
        if ((savingPodstation.getTransformatorsP() != null) && (savingPodstation.getTransformatorsP().size() > 0)) {
            for (int savingTransPNum = 0; savingTransPNum < savingPodstation.getTransformatorsP().size(); savingTransPNum++) {
                logger.info("processing transP {} ", savingPodstation.getTransformatorsP().get(savingTransPNum));
                updatedP = updatedP | updateTransformatorValues(savingPodstation.getTransformatorsP().get(savingTransPNum), savingTransPNum, INTERMEDIATE);
            }
        }
        boolean podstationUpdated = updated | updatedP;
        if (podstationUpdated) {
            currentPodstation = savingPodstation;
        }
        logger.info("podstation changed {}", podstationUpdated);
        return podstationUpdated;
    }

    @Override
    public boolean updateTransformatorValues(Transformator savingTransformator, int transNum, String additionalPostfix) {
        boolean updated = false;
        int sumiA, sumiB, sumiC, sumiO;
        sumiA = sumiB = sumiC = sumiO = 0;
        for (int savingLineNum = 0; savingLineNum < savingTransformator.getLines().size(); savingLineNum++) {
            Line savingLine = savingTransformator.getLines().get(savingLineNum);
            logger.info("processing {}", savingLine);
            sumiA += savingLine.getiA();
            sumiB += savingLine.getiB();
            sumiC += savingLine.getiC();
            sumiO += savingLine.getiO();
            if (!savingLine.equals(currentPodstation.getTransformators().get(transNum).getLines().get(savingLineNum))) {
                lineDAO.updateLineValues(savingLine, additionalPostfix);
                logger.info("updated {}", savingLine);
                updated = true;
            }
        }
        logger.info("sumA {} sumB {} sumC {} sumO {}", sumiA, sumiB, sumiC, sumiO);
        savingTransformator.setiA(sumiA);
        savingTransformator.setiB(sumiB);
        savingTransformator.setiC(sumiC);
        savingTransformator.setiN(sumiO);
        logger.info("saving trans {}", savingTransformator);
        logger.info("currentTrans {}", currentPodstation.getTransformators().get(transNum));
        logger.info("equals trans is {}", savingTransformator.equals(currentPodstation.getTransformators().get(transNum)));
        if (!savingTransformator.equals(currentPodstation.getTransformators().get(transNum))) {
            logger.info("updating {}", savingTransformator);
            transformatorDAO.updateTransformatorValues(savingTransformator, additionalPostfix);
            logger.info("updated {}", savingTransformator);
            updated = true;
        }
        return updated;
    }

    @Override
    public void createIntermediateMeasure(Podstation podstation) {
        for (Transformator transformator : podstation.getTransformators()) {
            logger.info("transformatorP {}", transformator);
            int transformatorPRn = transformatorDAO.addIntermediateTransformator(transformator);
            for (Line line : transformator.getLines()) {
                logger.info("lineP", line);
                lineDAO.addIntermediateLine(line, transformatorPRn);
            }
        }
        refreshCurrentPodstation();
    }

    @Override
    public void deleteIntermediateTransformator(int rn) {
        Transformator transformator = new Transformator();
        transformator.setRn(rn);
        transformatorDAO.deleteTransformator(transformator, INTERMEDIATE);
        lineDAO.deleteLines(transformator, INTERMEDIATE);
        refreshCurrentPodstation();
    }

    @Override
    public void addTransformator(Podstation podstation) {
        Transformator transformator = new Transformator();
        transformator.setTpRn(podstation.getRn());
        transformator.setNum(podstation.getTransformators().size() + 1);
        transformatorDAO.addTransformator(transformator, NORMAL);
        refreshCurrentPodstation();
    }

    @Override
    public void deleteTransformator(int rn) {
        transformatorDAO.deleteTransformator(rn, NORMAL);
        refreshCurrentPodstation();
    }

    @Override
    public void addLine(int transformatorRn) {
        int linesCount = 0;
        for (Transformator transformator : currentPodstation.getTransformators()) {
            if (transformator.getRn() == transformatorRn) {
                linesCount = transformator.getLines().size();
                break;
            }
        }
        Line line = new Line();
        line.setNum(linesCount + 1);
        line.setTrRn(transformatorRn);
        lineDAO.addLine(line, NORMAL);
        refreshCurrentPodstation();
    }

    @Override
    public void moveLine(int rn, String direction) {
        int shift = 0;
        if (direction.equals("up")) {
            shift = -1;
        } else if (direction.equals("down")) {
            shift = 1;
        }
        Line currentLine = lineDAO.getLine(rn, NORMAL);
        Line swappedLine;
        try {
            swappedLine = lineDAO.getLine(currentLine.getTrRn(), currentLine.getNum() + shift, NORMAL);
        } catch (EmptyResultDataAccessException e) {
            swappedLine = null;
        }
        if (swappedLine != null) {
            int currentLineNum = currentLine.getNum();
            int swappedLineNum = swappedLine.getNum();
            currentLine.setNum(swappedLineNum);
            swappedLine.setNum(currentLineNum);
            lineDAO.updateLineParams(currentLine);
            lineDAO.updateLineParams(swappedLine);
            refreshCurrentPodstation();
        }
    }

    @Override
    public void deleteLine(int rn) {
        Line line = new Line();
        line.setRn(rn);
        lineDAO.deleteLine(line, NORMAL);
        refreshCurrentPodstation();
    }

    @Override
    public boolean updatePodstationParams(Podstation savingPodstation) {
        boolean updated = false;
        if (!savingPodstation.equals(currentPodstation)) {
            podstationDAO.updatePodstationParams(savingPodstation);
            updated = true;
        }
        for (int savingTransNum = 0; savingTransNum < savingPodstation.getTransformators().size(); savingTransNum++) {
            Transformator savingTransformator = savingPodstation.getTransformators().get(savingTransNum);
            Transformator currentTransformator = currentPodstation.getTransformators().get(savingTransNum);
            if (!savingTransformator.equals(currentTransformator)) {
                transformatorDAO.updateTransformatorParams(savingTransformator);
                updated = true;
            }
            for (int savingLineNum = 0; savingLineNum < savingPodstation.getTransformators().get(savingTransNum).getLines().size(); savingLineNum++) {
                Line savingLine = savingPodstation.getTransformators().get(savingTransNum).getLines().get(savingLineNum);
                Line currentLine = currentPodstation.getTransformators().get(savingTransNum).getLines().get(savingLineNum);
                if (!savingLine.equals(currentLine)) {
                    lineDAO.updateLineParams(savingLine);
                    updated = true;
                }
            }
        }
        if (updated) {
            refreshCurrentPodstation();
        }
        return updated;
    }

}
