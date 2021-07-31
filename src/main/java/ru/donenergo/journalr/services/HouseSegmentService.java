package ru.donenergo.journalr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import ru.donenergo.journalr.dao.HouseSegmentDAO;
import ru.donenergo.journalr.mappers.StreetMapper;
import ru.donenergo.journalr.models.HouseSegment;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.models.Street;

import java.util.List;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HouseSegmentService implements IHouseSegmentService, IDataToModelSetter {

    private final HouseSegmentDAO houseSegmentDAO;
    private List<Street> streets;

    @Autowired
    public HouseSegmentService(HouseSegmentDAO houseSegmentDAO) {
        this.houseSegmentDAO = houseSegmentDAO;
    }

    @Override
    public void setDataToModel(Model model) {
        model.addAttribute("streets", getStreets());
    }

    public List<Street> getStreets() {
        if (streets == null) {
            streets = houseSegmentDAO.getStreets();
        }
        return streets;
    }

    @Override
    public List<HouseSegment> getHouseSegment(Podstation podstation) {
        return houseSegmentDAO.getHouseSegment(podstation);
    }

    @Override
    public List<HouseSegment> getHouseSegment(Podstation podstation, int trNum) {
        return houseSegmentDAO.getHouseSegment(podstation, trNum);
    }

    @Override
    public List<HouseSegment> getHouseSegment(Street street) {
        return houseSegmentDAO.getHouseSegment(street);
    }

    @Override
    public List<HouseSegment> getHouseSegment(Street street, Integer houseNum) {
        return houseSegmentDAO.getHouseSegment(street, houseNum);
    }

    @Override
    public Street getStreet(String streetName) {
        if (streetName.contains(", ")) {
            String[] streetParams = streetName.split(", ");
            return houseSegmentDAO.getStreet(streetParams[0], streetParams[1]);
        } else return new Street();
    }

    @Override
    public String addHouseSegment(HouseSegment houseSegment) {
        if (houseSegment.getHouse1().equals("")) {
            houseSegment.setHouse1("0");
        }
        if (houseSegment.getHouse2().equals("")) {
            houseSegment.setHouse2("0");
        }
        if (Integer.valueOf(houseSegment.getHouse2()) == 0) {
            houseSegment.setHouse2(houseSegment.getHouse1());
            houseSegment.setHouse2Building(houseSegment.getHouse1Building());
        }
        if (Integer.valueOf(houseSegment.getHouse1()) > Integer.valueOf(houseSegment.getHouse2())) {
            String exchangeNum = houseSegment.getHouse1();
            String exchangeBuilding = houseSegment.getHouse1Building();
            houseSegment.setHouse1(houseSegment.getHouse2());
            houseSegment.setHouse1Building(houseSegment.getHouse2Building());
            houseSegment.setHouse2(exchangeNum);
            houseSegment.setHouse2Building(exchangeBuilding);
        }
        houseSegmentDAO.addHouseSegment(houseSegment);
        return IMessageConstants.SEGMENT_ADD;
    }
    @Override
    public String deleteHouseSegment(int rn) {
        houseSegmentDAO.deleteHouseSegment(rn);
        return IMessageConstants.SEGMENT_DEL;
    }

}
