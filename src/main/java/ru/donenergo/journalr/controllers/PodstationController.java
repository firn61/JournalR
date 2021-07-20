package ru.donenergo.journalr.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.services.*;

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

    private void wrapData(Model model) {
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

    @Override
    public String editPodstationValues(Model model, @ModelAttribute("currentPodstation") Podstation podstation,
                                       @RequestParam(value = "action") String action) {
        if (action.equals("save")) {
            podstationService.updatePodstationValues(podstation);
            model.addAttribute("successMessage", IMessageConstants.PODSTATION_SAVED);
        } else if (action.equals("addP")) {

        } else if (action.startsWith("pTransDel")) {

        } else {

        }
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        wrapData(model);
        return "redirect:/" + commonService.getViewName();
    }
}
