package ru.donenergo.journalr.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import ru.donenergo.journalr.models.HouseSegment;
import ru.donenergo.journalr.models.Street;
import ru.donenergo.journalr.services.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HouseSegmentController implements IHouseSegmentController, IDataWrapper {

    private final HouseSegmentService houseSegmentService;
    private final PodstationService podstationService;
    private final HostRestrictionService hostRestrictionService;
    private final CommonService commonService;
    static final Logger logger = LoggerFactory.getLogger(HouseSegmentController.class);

    public HouseSegmentController(HouseSegmentService houseSegmentService, PodstationService podstationService, HostRestrictionService hostRestrictionService, CommonService commonService) {
        this.houseSegmentService = houseSegmentService;
        this.podstationService = podstationService;
        this.hostRestrictionService = hostRestrictionService;
        this.commonService = commonService;
    }

    @Override
    public void wrapData(Model model) {
        commonService.setDataToModel(model);
        hostRestrictionService.setDataToModel(model);
        podstationService.setDataToModel(model);
        houseSegmentService.setDataToModel(model);
    }

    @Override
    public String showHouseSegment(Model model, @RequestParam(value = "street", required = false) String streetName,
                                   @RequestParam(value = "housenum", required = false) Integer houseNum) {
        logger.info("streetName {}, houseNum {}", streetName, houseNum);
        List<HouseSegment> houseSegments;
        String segmentSource;
        if ((streetName == null) || (streetName.length() == 0)) {
            houseSegments = houseSegmentService.getHouseSegment(podstationService.getCurrentPodstation());
            segmentSource = podstationService.getCurrentPodstation().getDispName();
        } else {
            Street street = houseSegmentService.getStreet(streetName);
            segmentSource = street.getStreetType() + ", " + street.getStreetName();
            model.addAttribute("selectedStreet", street.getRn());
            if (houseNum == null) {
                houseSegments = houseSegmentService.getHouseSegment(street);
            } else {
                houseSegments = houseSegmentService.getHouseSegment(street, houseNum);
                segmentSource = segmentSource + ", " + houseNum;
            }
        }
        model.addAttribute("houseSegments", houseSegments);
        if (houseSegments.size() > 0) {
            commonService.addMessage(IMessageConstants.SEGMENT + segmentSource);
        } else {
            commonService.addError(IMessageConstants.SEGMENT + segmentSource);
        }
        wrapData(model);
        return "showhousesegment";
    }

    //GET method
    @Override
    public String editHouseSegment(Model model, @RequestParam(value = "rn") int podstationRn) {
        model.addAttribute("houseSegments", houseSegmentService.getHouseSegment(podstationService.getCurrentPodstation()));
        wrapData(model);
        return "edithousesegment";
    }

    //POST method
    @Override
    public String editHouseSegment(Model model, HouseSegment houseSegment) {
        return null;
    }
}
