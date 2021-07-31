package ru.donenergo.journalr.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.donenergo.journalr.mappers.LineMapper;
import ru.donenergo.journalr.models.Line;
import ru.donenergo.journalr.models.Transformator;

import java.util.List;

@Repository
public class LineDAO implements ILineDAO{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LineDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Line getLine(int rn, String additionalPostfix) {
        String queryTemplate = "SELECT RN, TR_RN, NUM, NAME, I_A, I_B, I_C, I_O, KA FROM LINE" + additionalPostfix +" WHERE RN = ?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{rn}, new LineMapper());
    }

    @Override
    public Line getLine(int trRn, int num, String additionalPostfix) {
        String queryTemplate = "SELECT RN, TR_RN, NUM, NAME, I_A, I_B, I_C, I_O, KA FROM LINE" + additionalPostfix + " WHERE TR_RN =? AND NUM = ?";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{trRn, num}, new LineMapper());
    }

    @Override
    public List<Line> getLines(int trRn, String additionalPostfix) {
        String queryTemplate = "SELECT RN, TR_RN, NUM, NAME, I_A, I_B, I_C, I_O, KA FROM LINE" + additionalPostfix + " WHERE TR_RN = ? ORDER BY NUM";
        return jdbcTemplate.query(queryTemplate, new Object[]{trRn}, new LineMapper());
    }

    @Override
    public Integer addLine(Line line, String additionalPostfix) {
        String queryTemplate ="execute procedure LINE_INSERT" + additionalPostfix +"(?, ?, ?, 0, 0, 0, 0, null)";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{line.getTrRn(), line.getNum(), "Л-"}, Integer.class);
    }

    @Override
    public Integer addIntermediateLine(Line line, int trRn){
        String queryTemplate ="execute procedure LINE_INSERT_P(?, ?, ?, 0, 0, 0, 0, null)";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{trRn, line.getNum(), line.getName()}, Integer.class);
    }

    @Override
    public Integer addLineToNewPeriod(int trRn, Line line) {
        String queryTemplate = "execute procedure LINE_INSERT(?, ?, ?, 0, 0, 0, 0, null)";
        return jdbcTemplate.queryForObject(queryTemplate, new Object[]{trRn, line.getNum(), line.getNum()}, Integer.class);
    }

    @Override
    public void updateLineValues(Line line, String additionalPostfix) {
        String queryTemplate = "execute procedure LINE_VALUESUPDATE" + additionalPostfix + "(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(queryTemplate, new Object[]{line.getRn(), line.getiA(), line.getiB(), line.getiC(), line.getiO(), line.getkA()});
    }

    @Override
    public void updateLineParams(Line line) {
        String queryTemplate = "execute procedure LINE_UPDATE(?, ?, ?)";
        jdbcTemplate.update(queryTemplate, new Object[]{line.getNum(), line.getName(), line.getRn()});
    }

    @Override
    public void deleteLine(Line line, String additionalPostfix) {
        String queryTemplate = "DELETE FROM LINE" + additionalPostfix + " WHERE RN = ?";
        jdbcTemplate.update(queryTemplate, new Object[]{line.getRn()});
    }

    @Override
    public void deleteLines(Transformator transformator, String additionalPostfix) {
        String queryTemplate = "DELETE FROM LINE" + additionalPostfix + " WHERE TR_RN = ?";
        jdbcTemplate.update(queryTemplate, new Object[]{transformator.getRn()});
    }
}
