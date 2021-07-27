package ru.donenergo.journalr.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.donenergo.journalr.mappers.HouseSegmentMapper;
import ru.donenergo.journalr.mappers.StreetMapper;
import ru.donenergo.journalr.models.HouseSegment;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Street;
import ru.donenergo.journalr.models.Transformator;

import java.util.List;

@Repository
public class HouseSegmentDAO implements IHouseSegmentDAO, IStreetDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HouseSegmentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<HouseSegment> getHouseSegment(Podstation podstation) {
        String queryTemplate = "SELECT RN, STR_PODSTATION, TR_NUM, FIDER, STREET_RN, STREET_NAME, STREET_TYPE, HOUSE1, " +
                "HOUSE1BUILDING, HOUSE2, HOUSE2BUILDING FROM HOUSE_SEGMENT WHERE STR_PODSTATION = ? ORDER BY STREET_NAME";
        return jdbcTemplate.query(queryTemplate, new Object[]{podstation.getPodstType() + podstation.getNumStr()}, new HouseSegmentMapper());
    }

    @Override
    public List<HouseSegment> getHouseSegment(Podstation podstation, int trNum) {
        String queryTemplate = "SELECT RN, STR_PODSTATION, TR_NUM, FIDER, STREET_RN, STREET_NAME, STREET_TYPE, HOUSE1, " +
                "HOUSE1BUILDING, HOUSE2, HOUSE2BUILDING FROM HOUSE_SEGMENT WHERE STR_PODSTATION = ? AND TR_NUM = ? ORDER BY STREET_NAME";
        return jdbcTemplate.query(queryTemplate, new Object[]{podstation.getPodstType() + podstation.getNumStr(), trNum}, new HouseSegmentMapper());
    }

    @Override
    public List<HouseSegment> getHouseSegment(Street street) {
        String queryTemplate = "SELECT RN, STR_PODSTATION, TR_NUM, FIDER, STREET_RN, STREET_NAME, STREET_TYPE, HOUSE1, " +
                "HOUSE1BUILDING, HOUSE2, HOUSE2BUILDING FROM HOUSE_SEGMENT WHERE STREET_NAME = ? AND STREET_TYPE = ? ORDER BY HOUSE1";
        return jdbcTemplate.query(queryTemplate, new Object[]{street.getStreetName(), street.getStreetType()}, new HouseSegmentMapper());
    }

    @Override
    public List<HouseSegment> getHouseSegment(Street street, Integer houseNum) {
        String queryTemplate = "SELECT RN, STR_PODSTATION, TR_NUM, FIDER, STREET_RN, STREET_NAME, STREET_TYPE, HOUSE1, " +
                "HOUSE1BUILDING, HOUSE2, HOUSE2BUILDING FROM HOUSE_SEGMENT WHERE STREET_NAME = ? AND STREET_TYPE = ? AND (HOUSE1 <= ? AND HOUSE2 >= ? ) ORDER BY HOUSE1";
        return jdbcTemplate.query(queryTemplate, new Object[]{street.getStreetName(), street.getStreetType(), houseNum, houseNum}, new HouseSegmentMapper());
    }

    @Override
    public Integer addHouseSegment(HouseSegment houseSegment) {
        String queryTemplate = "execute procedure SEGMENT_INSERT(?, ?, ?, ? ,?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.queryForObject(queryTemplate,
                new Object[]{houseSegment.getStrPodstation(), houseSegment.getTrNum(), houseSegment.getFider(),
                        houseSegment.getStreetRn(), houseSegment.getStreetName(), houseSegment.getStreetType(),
                        houseSegment.getHouse1(), houseSegment.getHouse1Building(), houseSegment.getHouse2(),
                        houseSegment.getHouse2Building()}, Integer.class);
    }

    @Override
    public void deleteHouseSegment(int rn) {
        String queryTemplate = "execute procedure SEGMENT_DELETE(?)";
        jdbcTemplate.update(queryTemplate, new Object[]{rn});
    }

    @Override
    public Integer getStreetRn(Street street) {
        String queryTemplate = "SELECT RN FROM STREETS WHERE STREET_NAME = ? AND STREET_TYPE = ?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{street.getStreetName(), street.getStreetType()}, Integer.class);
    }

    @Override
    public List<Street> getStreets() {
        return jdbcTemplate.query("SELECT RN, STREET_NAME, STREET_TYPE, POSTCODE FROM STREETS", new StreetMapper());
    }

    public Street getStreet(String streetName, String streetType) {
        String queryTemplate = "SELECT RN, STREET_NAME, STREET_TYPE, POSTCODE FROM STREETS WHERE STREET_NAME = ? AND STREET_TYPE = ?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{streetName, streetType}, new StreetMapper());
    }

}
