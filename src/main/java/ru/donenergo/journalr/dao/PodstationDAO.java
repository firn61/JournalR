package ru.donenergo.journalr.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.donenergo.journalr.mappers.PodstationMapper;
import ru.donenergo.journalr.models.Podstation;

import java.util.List;

@Repository
public class PodstationDAO implements IPodstationDAO{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PodstationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Podstation getPodstation(int rn) {
        String queryTemplate = "SELECT RN, PODST_TYPE, NUM, NUM_STR, RES_NUM, DATE_RN, IS_ACTIVE, ADDRESS FROM PODSTATION WHERE RN =?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{rn}, new PodstationMapper());
    }

    @Override
    public Integer getPodstationRn(int num, String type, int dateRn) throws EmptyResultDataAccessException {
        String queryTemplate = "SELECT RN FROM PODSTATION WHERE PODST_TYPE = ? AND NUM_STR = ? AND DATE_RN = ?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{type, num, dateRn}, Integer.class);
    }

    @Override
    public Integer podstationCount(Podstation podstation) {
        String queryTemplate = "SELECT COUNT(RN) FROM PODSTATION WHERE PODST_TYPE = ? AND NUM = ? AND DATE_RN = ?";
        return jdbcTemplate.queryForObject(queryTemplate,
                new Object[]{podstation.getPodstType(), podstation.getNum(), podstation.getDateRn()}, Integer.class);
    }

    @Override
    public Integer addPodstation(Podstation podstation) {
        String queryTemplate = "execute procedure PODST_INSERT(?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.queryForObject(queryTemplate,
                new Object[]{podstation.getPodstType(), podstation.getNum(), podstation.getNumStr(), podstation.getResNum(),
                             podstation.getDateRn(), 0, podstation.getAddress()}, Integer.class);
    }

    @Override
    public void updatePodstationParams(Podstation podstation) {
        String queryTemplate = "execute procedure PODST_UPDATE(?, ?)";
        jdbcTemplate.update(queryTemplate, new Object[]{podstation.getAddress(), podstation.getRn()});
    }

    public List<Podstation> getPodstations(int currentDate) {
        String queryTemplate = "SELECT RN, PODST_TYPE, NUM, NUM_STR, RES_NUM, DATE_RN, IS_ACTIVE, ADDRESS FROM PODSTATION WHERE DATE_RN=? ORDER BY PODST_TYPE, NUM";
        return jdbcTemplate.query(queryTemplate, new Object[]{currentDate}, new PodstationMapper());
    }


}
