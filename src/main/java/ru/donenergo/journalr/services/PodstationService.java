package ru.donenergo.journalr.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import ru.donenergo.journalr.dao.ILineDAO;
import ru.donenergo.journalr.dao.IPodstationDAO;
import ru.donenergo.journalr.dao.ITransformatorDAO;
import ru.donenergo.journalr.exceptions.PodstationAlreadyExistException;
import ru.donenergo.journalr.models.BasicPodstation;
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
    public void refreshCurrentPodstation() {
        currentPodstation = getPodstation(currentPodstation.getRn());
    }

    @Override
    public int getFirstTransformator() {
        if (currentPodstation.getTransformators() != null) {
            return 1;
        } else {
            return 0;
        }
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

    public Podstation getCurrentPodstation() {
        return currentPodstation;
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
        model.addAttribute("currentPodstation", currentPodstation);
    }

    public String getCurrentPodstationShortName() {
        return currentPodstation.getPodstType() + currentPodstation.getNumStr();
    }

    @Override
    public String getCurrentPodstationFromNewPeriod(int period) {
        try {
            Integer podstationRn = podstationDAO.getPodstationRn(currentPodstation.getNum(), currentPodstation.getPodstType(), period);
            currentPodstation = getPodstation(podstationRn);
            return "ok";
        } catch (EmptyResultDataAccessException e) {
            return currentPodstation.getPodstType() + "-" + currentPodstation.getNum() + IMessageConstants.PODSTATION_NOT_FOUND_NP;
        }

    }

    @Override
    public String getPodstationByNumAndType(int num, String podstType, int period) {
        try {
            Integer podstationRn = podstationDAO.getPodstationRn(num, podstType, period);
            currentPodstation = getPodstation(podstationRn);
            return "ok";
        } catch (EmptyResultDataAccessException e) {
            return podstType + "-" + num + IMessageConstants.PODSTATION_NOT_FOUND;
        }
    }

    @Override
    public void podstationRnCheck(int rn) {
        if (currentPodstation.getRn() != rn) {
            currentPodstation = getPodstation(rn);
            logger.warn("current podstation updated to {}", rn);
        }
    }

    @Override
    public String updatePodstationValues(Podstation savingPodstation) {
        boolean updated = false;
        boolean updatedP = false;
        if ((savingPodstation.getTransformators() != null) && (savingPodstation.getTransformators().size() > 0)) {
            for (int savingTransNum = 0; savingTransNum < savingPodstation.getTransformators().size(); savingTransNum++) {
                logger.info("processing trans {} ", savingPodstation.getTransformators().get(savingTransNum));
                updated = updated | updateTransformatorValues(savingPodstation.getTransformators().get(savingTransNum), savingTransNum, NORMAL);
            }
        }
        if ((savingPodstation.getTransformatorsP() != null) && (savingPodstation.getTransformatorsP().size() > 0)) {
            logger.info("saving transP count {}", savingPodstation.getTransformatorsP().size());
            for (int savingTransPNum = 0; savingTransPNum < savingPodstation.getTransformatorsP().size(); savingTransPNum++) {
                logger.info("saving transP num {}", savingTransPNum);
                logger.info("processing transP {} ", savingPodstation.getTransformatorsP().get(savingTransPNum));
                updatedP = updatedP | updateTransformatorValues(savingPodstation.getTransformatorsP().get(savingTransPNum), savingTransPNum, INTERMEDIATE);
            }
        }
        boolean podstationUpdated = updated | updatedP;
        if (podstationUpdated) {
            currentPodstation = savingPodstation;
        }
        logger.info("podstation changed {}", podstationUpdated);
        return podstationUpdated ? IMessageConstants.PODSTATION_SAVED : IMessageConstants.PODSTATION_NOTNING_TO_SAVE;
    }

    @Override
    public boolean updateTransformatorValues(Transformator savingTransformator, int transNum, String additionalPostfix) {
        boolean updated = false;
        int sumiA, sumiB, sumiC, sumiO;
        sumiA = sumiB = sumiC = sumiO = 0;
        for (int savingLineNum = 0; savingLineNum < savingTransformator.getLines().size(); savingLineNum++) {
            logger.info("saving line num {}", savingLineNum);
            Line savingLine = savingTransformator.getLines().get(savingLineNum);
            logger.info("processing {}", savingLine);
            sumiA += savingLine.getiA();
            sumiB += savingLine.getiB();
            sumiC += savingLine.getiC();
            sumiO += savingLine.getiO();
            Line currentLine = new Line();
            if (additionalPostfix.equals(NORMAL)) {
                currentLine = currentPodstation.getTransformators().get(transNum).getLines().get(savingLineNum);
            } else {
                for (int transPNum = 0; transPNum < currentPodstation.getTransformatorsP().size(); transPNum++) {
                    if (currentPodstation.getTransformatorsP().get(transPNum).getRn() == savingTransformator.getRn()) {
                        currentLine = currentPodstation.getTransformatorsP().get(transPNum).getLines().get(savingLineNum);
                        break;
                    }
                }
            }
            if (!savingLine.equals(currentLine)) {
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
        Transformator currentTransformator = new Transformator();
        if (additionalPostfix.equals(NORMAL)) {
            currentTransformator = currentPodstation.getTransformators().get(transNum);
        } else {
            for (int transPNum = 0; transPNum < currentPodstation.getTransformatorsP().size(); transPNum++) {
                if (currentPodstation.getTransformatorsP().get(transPNum).getRn() == savingTransformator.getRn()) {
                    currentTransformator = currentPodstation.getTransformatorsP().get(transPNum);
                    logger.info("saving trans from intermediate {}", currentTransformator);
                }
            }
        }
        if (!savingTransformator.equals(currentTransformator)) {
            transformatorDAO.updateTransformatorValues(savingTransformator, additionalPostfix);
            logger.info("updated {}", savingTransformator);
            updated = true;
        }
        return updated;
    }

    @Override
    public String createIntermediateMeasure(Podstation podstation) {
        for (Transformator transformator : podstation.getTransformators()) {
            logger.info("transformatorP {}", transformator);
            int transformatorPRn = transformatorDAO.addIntermediateTransformator(transformator);
            for (Line line : transformator.getLines()) {
                logger.info("lineP", line);
                lineDAO.addIntermediateLine(line, transformatorPRn);
            }
        }
        refreshCurrentPodstation();
        return IMessageConstants.INTERMEDIATE_ADD;
    }

    @Override
    public String deleteIntermediateTransformator(int rn) {
        Transformator transformator = new Transformator();
        transformator.setRn(rn);
        transformatorDAO.deleteTransformator(transformator, INTERMEDIATE);
        lineDAO.deleteLines(transformator, INTERMEDIATE);
        refreshCurrentPodstation();
        return IMessageConstants.INTERMEDIATE_DEL;
    }

    @Override
    public String addTransformator(Podstation podstation) {
        Transformator transformator = new Transformator();
        transformator.setTpRn(podstation.getRn());
        int transNum;
        if (podstation.getTransformators() == null) {
            transNum = 1;
        } else {
            transNum = podstation.getTransformators().size() + 1;
        }
        transformator.setNum(transNum);
        transformatorDAO.addTransformator(transformator, NORMAL);
        refreshCurrentPodstation();
        return IMessageConstants.TRANSFORMATOR_ADD;
    }

    @Override
    public String deleteTransformator(int rn) {
        transformatorDAO.deleteTransformator(rn, NORMAL);
        refreshCurrentPodstation();
        return IMessageConstants.TRANSFORMATOR_DEL;
    }

    @Override
    public String addLine(int transformatorRn) {
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
        return IMessageConstants.LINE_ADD;
    }

    @Override
    public String moveLine(int rn, String direction) {
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
            return shift == -1 ? IMessageConstants.LINE_MOVED_UP : IMessageConstants.LINE_MOVED_DOWN;
        } else {
            return IMessageConstants.LINE_CANNOT_MOVED;
        }
    }

    @Override
    public String deleteLine(int rn) {
        Line line = new Line();
        line.setRn(rn);
        lineDAO.deleteLine(line, NORMAL);
        refreshCurrentPodstation();
        return IMessageConstants.LINE_DEL;
    }

    @Override
    public String updatePodstationParams(Podstation savingPodstation) {
        boolean updated = false;
        if (!savingPodstation.equals(currentPodstation)) {
            podstationDAO.updatePodstationParams(savingPodstation);
            logger.info("updating podstation {}", savingPodstation);
            logger.info("saving podstation {}", currentPodstation);
            updated = true;
        }
        for (int savingTransNum = 0; savingTransNum < savingPodstation.getTransformators().size(); savingTransNum++) {
            Transformator savingTransformator = savingPodstation.getTransformators().get(savingTransNum);
            Transformator currentTransformator = currentPodstation.getTransformators().get(savingTransNum);
            if (!savingTransformator.equals(currentTransformator)) {
                transformatorDAO.updateTransformatorParams(savingTransformator);
                logger.info("updating transformator {}", savingTransformator);
                logger.info("current transformator {}", currentTransformator);
                updated = true;
            }
            for (int savingLineNum = 0; savingLineNum < savingPodstation.getTransformators().get(savingTransNum).getLines().size(); savingLineNum++) {
                Line savingLine = savingPodstation.getTransformators().get(savingTransNum).getLines().get(savingLineNum);
                Line currentLine = currentPodstation.getTransformators().get(savingTransNum).getLines().get(savingLineNum);
                if (!savingLine.equals(currentLine)) {
                    lineDAO.updateLineParams(savingLine);
                    logger.info("updating line {}", savingLine);
                    logger.info("current line {}", currentLine);
                    updated = true;
                }
            }
        }
        if (updated) {
            refreshCurrentPodstation();
        }
        return updated ? IMessageConstants.PODSTATION_SAVED : IMessageConstants.PODSTATION_NOTNING_TO_SAVE;
    }

    @Override
    public BasicPodstation addPodstation(String podstType, int num, String address, int dateRn, int resNum) throws PodstationAlreadyExistException {
        Podstation podstation = new Podstation();
        podstation.setPodstType(podstType);
        podstation.setNum(num);
        podstation.setNumStr(String.valueOf(num));
        podstation.setAddress(address);
        podstation.setDateRn(dateRn);
        podstation.setResNum(resNum);
        if (podstationDAO.podstationCount(podstation) == 0) {
            podstation.setRn(podstationDAO.addPodstation(podstation));
            currentPodstation = podstation;
        } else {
            throw new PodstationAlreadyExistException(IMessageConstants.PODSTATION_ALREADY_EXIST);
        }
        return podstation.convertToBasic();
    }

}
