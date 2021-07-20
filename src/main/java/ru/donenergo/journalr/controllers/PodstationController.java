package ru.donenergo.journalr.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.donenergo.journalr.services.Activity;
import ru.donenergo.journalr.services.CommonService;
import ru.donenergo.journalr.services.HostRestrictionService;
import ru.donenergo.journalr.services.PodstationService;

@Controller
public class PodstationController implements IPodstationController {

    private final CommonService commonService;
    private final HostRestrictionService hostRestrictionService;
    private final PodstationService podstationService;
    static final Logger logger = LoggerFactory.getLogger(PodstationController.class);

    @Autowired
    public PodstationController(CommonService commonService, HostRestrictionService hostRestrictionService, PodstationService podstationService) {
        this.commonService = commonService;
        this.hostRestrictionService = hostRestrictionService;
        this.podstationService = podstationService;
    }

    private void wrapData(Model model){
        commonService.setDataToModel(model);
        hostRestrictionService.setDataToModel(model);
        podstationService.setDataToModel(model);
    }

    @Override
    public String showPodstation(Model model, @RequestParam(value = "rn") int rn) {
        podstationService.podstationRnCheck(rn);
        wrapData(model);
        return "showpodstation";
    }

    @Override
    public String editPodstationValues(Model model, @RequestParam(value = "rn") int rn) {
        podstationService.podstationRnCheck(rn);
        wrapData(model);
        return "editpodstationvalues";
    }

    @Override
    public String editPodstationParams(Model model, @RequestParam(value = "rn") int rn) {
        podstationService.podstationRnCheck(rn);
        wrapData(model);
        return "editpodstationparams";
    }

    @GetMapping("/displaypodstation")
    public String displayPodstation(Model model, @RequestParam(value = "rn") int rn){
        if (podstationService.getCurrentPodstationRn() != rn) {
            System.out.println("not equals");
        }
        wrapData(model);
//        commonService.setDataToModel(model);
//        hostRestrictionService.setDataToModel(model);
//        podstationService.setDataToModel(model);
        return commonService.getViewName();
    }

}
