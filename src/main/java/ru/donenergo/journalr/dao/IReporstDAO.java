package ru.donenergo.journalr.dao;

import java.util.List;

public interface IReporstDAO {

    List<String[]> getReportAllPodstations(int dateRn);

    List<String[]> getOverloadedPodstations(int dateRn);

}
