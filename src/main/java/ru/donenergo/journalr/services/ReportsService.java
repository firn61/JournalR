package ru.donenergo.journalr.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.donenergo.journalr.dao.IReporstDAO;
import ru.donenergo.journalr.dao.ReportsDAO;
import ru.donenergo.journalr.models.Podstation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ReportsService implements IReportsService {

    private final ReportsDAO reportsDAO;
    static final Logger logger = LoggerFactory.getLogger(ReportsService.class);

    public ReportsService(ReportsDAO reportsDAO) {
        this.reportsDAO = reportsDAO;
    }

    @Override
    public  List<String[]> getBlankReportLines(Podstation podstation) {
        List<String[]> result = new ArrayList<>();
        int trNum = podstation.getTransformators().size();
        int maxRowCount = 0;
        if (trNum == 0) {
            return result;
        } else {
            List<Integer> rowCounts = new ArrayList<>();
            for (int i = 0; i < trNum; i++) {
                rowCounts.add(podstation.getTransformators().get(i).getLines().size());
            }
            maxRowCount = Collections.max(rowCounts);
            for (int i = 0; i < maxRowCount; i++) {
                result.add(new String[trNum]);
            }
            for (int i = 0; i < trNum; i++) {
                for (int j = 0; j < podstation.getTransformators().get(i).getLines().size(); j++) {
                    result.get(j)[i] = podstation.getTransformators().get(i).getLines().get(j).getName();
                }
            }
        }
        return result;
    }

    @Override
    public List<String[]> getReportAllPodstations(int currentDate){
        return reportsDAO.getReportAllPodstations(currentDate);
    }

    @Override
    public List<String[]> getOverloadedPodstations(int currentDate){
        return reportsDAO.getOverloadedPodstations(currentDate);
    }

    @Override
    public List<String[]> getMeasureReport(Podstation p) {
        logger.info("podstation: {}", p);
        int[] rowPrefix = new int[p.getTransformators().size()];
        rowPrefix[0] = 0;
        logger.info("transformators num: {}", p.getTransformators().size());
        for (int i = 1; i < p.getTransformators().size(); i++) {
            if (p.getTransformators().get(i).getLines().size() <= 9) {
                rowPrefix[i] = 9;
            } else {
                rowPrefix[i] = p.getTransformators().get(i).getLines().size();
            }
        }
        List<String[]> result = new ArrayList<>();
        for (int i = 0; i < p.getTransformators().size(); i++) {
            if (p.getTransformators().get(i).getLines().size() <= 9) {
                for (int j = 0; j < 9; j++) {
                    result.add(new String[11]);
                }
            } else {
                for (int j = 0; j < p.getTransformators().get(i).getLines().size(); j++) {
                    result.add(new String[11]);
                }
            }
            result.get(0+rowPrefix[i])[10] = p.getTransformators().get(i).getMonter();
            result.get(0+rowPrefix[i])[0] = "T-" + p.getTransformators().get(i).getNum() + ", " + p.getTransformators().get(i).getPower() + " " + p.getTransformators().get(i).getFider();
            result.get(0+rowPrefix[i])[1] = "A";
            result.get(0+rowPrefix[i])[2] = p.getTransformators().get(i).getuA() + "";
            result.get(0+rowPrefix[i])[3] = p.getTransformators().get(i).getiA() + "";
            result.get(2+rowPrefix[i])[1] = "B";
            result.get(2+rowPrefix[i])[2] = p.getTransformators().get(i).getuB() + "";
            result.get(2+rowPrefix[i])[3] = p.getTransformators().get(i).getiB() + "";
            result.get(4+rowPrefix[i])[1] = "C";
            result.get(4+rowPrefix[i])[2] = p.getTransformators().get(i).getuC() + "";
            result.get(4+rowPrefix[i])[3] = p.getTransformators().get(i).getiC() + "";
            result.get(6+rowPrefix[i])[1] = "N";
            for (int j = 0; j < p.getTransformators().get(i).getLines().size(); j++) {
                result.get(j+rowPrefix[i])[4] = p.getTransformators().get(i).getLines().get(j).getName();
                result.get(j+rowPrefix[i])[5] = p.getTransformators().get(i).getLines().get(j).getkA();
                result.get(j+rowPrefix[i])[6] = p.getTransformators().get(i).getLines().get(j).getiA() + "";
                result.get(j+rowPrefix[i])[7] = p.getTransformators().get(i).getLines().get(j).getiB() + "";
                result.get(j+rowPrefix[i])[8] = p.getTransformators().get(i).getLines().get(j).getiC() + "";
                result.get(j+rowPrefix[i])[9] = p.getTransformators().get(i).getLines().get(j).getiO() + "";
            }
        }
        logger.info("result size {}", result.size());
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i)[0] + " " +
                    result.get(i)[1] + " " +
                    result.get(i)[2] + " " +
                    result.get(i)[3] + " " +
                    result.get(i)[4] + " " +
                    result.get(i)[5] + " " +
                    result.get(i)[6] + " " +
                    result.get(i)[7] + " " +
                    result.get(i)[8] + " " +
                    result.get(i)[9] + " " +
                    result.get(i)[10]
            );
        }
        return result;
    }

}
