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

    public List<HouseSegment> getHouseSegment(Podstation podstation) {
        return houseSegmentDAO.getHouseSegment(podstation);
    }

    public List<HouseSegment> getHouseSegment(Street street) {
        return houseSegmentDAO.getHouseSegment(street);
    }
    public List<HouseSegment> getHouseSegment(Street street, Integer houseNum) {
        return houseSegmentDAO.getHouseSegment(street, houseNum);
    }

    public Street getStreet(String streetName){
        String[] streetParams = streetName.split(", ");
        return houseSegmentDAO.getStreet(streetParams[0], streetParams[1]);
    }

    public void addHouseSegment(HouseSegment houseSegment){

        houseSegmentDAO.addHouseSegment(houseSegment);
    }

}
