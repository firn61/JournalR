package ru.donenergo.journalr.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.donenergo.journalr.mappers.BasicPodstationMapper;
import ru.donenergo.journalr.mappers.PeriodMapper;
import ru.donenergo.journalr.models.BasicPodstation;
import ru.donenergo.journalr.models.Period;

import java.util.List;

@Repository
public class CommonDAO implements ICommonDAO{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getCurrentPeriod() {
        return jdbcTemplate.queryForObject("SELECT SVALUE FROM SYSTEM WHERE SPARAM = 'currentDate'", Integer.class);
    }

    @Override
    public List<BasicPodstation> getPodstationLabels(int dateRn) {
        String queryTemplate = "SELECT RN, PODST_TYPE, NUM FROM PODSTATION WHERE DATE_RN=? ORDER BY PODST_TYPE, NUM";
        return jdbcTemplate.query(queryTemplate, new Object[]{dateRn}, new BasicPodstationMapper());
    }

    @Override
    public List<String> getPodstationTypes(int dateRn) {
        String queryTemplate = "SELECT DISTINCT PODST_TYPE FROM PODSTATION WHERE DATE_RN = ?";
        return jdbcTemplate.queryForList(queryTemplate, new Object[]{dateRn}, String.class);
    }

    @Override
    public List<Period> getPeriods(int dateRn) {
        return jdbcTemplate.query("SELECT RN, SDATE FROM DATES WHERE RN <= ?", new Object[]{dateRn}, new PeriodMapper());
    }
}
