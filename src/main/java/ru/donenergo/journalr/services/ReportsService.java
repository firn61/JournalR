package ru.donenergo.journalr.services;

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

}
