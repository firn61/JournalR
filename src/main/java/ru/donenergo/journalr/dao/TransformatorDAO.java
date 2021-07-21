package ru.donenergo.journalr.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.donenergo.journalr.mappers.TransformatorMapper;
import ru.donenergo.journalr.models.Transformator;

import java.util.List;

@Repository
public class TransformatorDAO implements ITransformatorDAO{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransformatorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transformator getTransformator(int rn, String additionalPostfix) {
        String queryTemplate = "SELECT RN, TP_RN, NUM, FIDER, POWER, U_A, U_B, U_C, I_A, I_B, I_C, I_N, DATETIME, MONTER FROM TRANSFORMATOR" + additionalPostfix + " WHERE RN=?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{rn}, new TransformatorMapper());
    }

    @Override
    public Transformator getTransformator(int tpRn, int num, String additionalPostfix) {
        String queryTemplate = "SELECT RN, TP_RN, NUM, FIDER, POWER, U_A, U_B, U_C, I_A, I_B, I_C, I_N, DATETIME, MONTER FROM TRANSFORMATOR" + additionalPostfix + " WHERE TP_RN=? AND NUM=?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{tpRn, num}, new TransformatorMapper());
    }

    @Override
    public List<Transformator> getTransformators(int tpRn, String additionalPostfix) {
        String queryTemplate = "SELECT RN, TP_RN, NUM, FIDER, POWER, U_A, U_B, U_C, I_A, I_B, I_C, I_N, DATETIME, MONTER FROM TRANSFORMATOR" + additionalPostfix + " WHERE TP_RN=? ORDER BY NUM";
        return jdbcTemplate.query(queryTemplate, new Object[]{tpRn}, new TransformatorMapper());
    }

    @Override
    public Integer addTransformator(Transformator transformator, String additionalPostfix) {
        String queryTemplate = "execute procedure TRANS_INSERT" + additionalPostfix +"(?, ?, null, 0, 0, 0, 0, 0, 0, 0, 0, null, null)";
                return jdbcTemplate.queryForObject(queryTemplate, new Object[]{transformator.getTpRn(), transformator.getNum()}, Integer.class);
    }

    @Override
    public Integer addIntermediateTransformator(Transformator transformator){
        String queryTemplate = "execute procedure TRANS_INSERT_P(?, ?, ?, ?, 0, 0, 0, 0, 0, 0, 0, null, null)";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{transformator.getTpRn(), transformator.getNum(), transformator.getFider(), transformator.getPower()}, Integer.class);
    }

    @Override
    public Integer addTransformatorToNewPeriod(Transformator transformator) {
        String queryTemplate = "execute procedure TRANS_INSERT(?, ?, ?, ?, 0, 0, 0, 0, 0, 0, 0, null, null)";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{transformator.getTpRn(), transformator.getNum(),
                transformator.getFider(), transformator.getPower()}, Integer.class);
    }

    @Override
    public void updateTransformatorValues(Transformator transformator, String additionalPostfix) {
        String queryTemplate = "execute procedure TRANS_VALUESUPDATE"+ additionalPostfix + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(queryTemplate, new Object[]{transformator.getRn(), transformator.getuA(), transformator.getuB(),
                transformator.getuC(), transformator.getiA(), transformator.getiB(), transformator.getiC(), transformator.getiN(),
                        transformator.getDateTimeToDAO(), transformator.getMonter()});
    }

    @Override
    public void updateTransformatorParams(Transformator transformator) {
        String queryTemplate = "execute procedure TRANS_UPDATE(?, ?, ?)";
        jdbcTemplate.update(queryTemplate, new Object[]{transformator.getFider(), transformator.getPower(), transformator.getRn()});
    }

    @Override
    public void deleteTransformator(Transformator transformator, String additionalPostfix) {
        String queryTemplate = "DELETE FROM TRANSFORMATOR" + additionalPostfix + " WHERE RN = ?";
        jdbcTemplate.update(queryTemplate, new Object[]{transformator.getRn()});
    }

    @Override
    public void deleteTransformator(int rn, String additionalPostfix) {
        Transformator transformator = new Transformator();
        transformator.setRn(rn);
        deleteTransformator(transformator, additionalPostfix);
    }

    @Override
    public boolean isTransformatorPExist(int rn) {
        Integer transformatorsPCount = jdbcTemplate.queryForObject("SELECT count(RN) FROM TRANSFORMATOR_P WHERE TP_RN = ?", new Object[]{rn}, Integer.class);
        return (transformatorsPCount > 0);
    }
}
