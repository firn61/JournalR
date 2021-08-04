package ru.donenergo.journalr.services;

import ru.donenergo.journalr.models.Podstation;

import java.util.List;

public interface IReportsService {

    List<String[]> getBlankReportLines(Podstation podstation);

    List<String[]> getReportAllPodstations(int currentDate);

    List<String[]> getOverloadedPodstations(int currentDate);
}
