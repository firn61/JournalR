package ru.donenergo.journalr.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Override
    public String changePeriod(Model model, @RequestParam(value = "period") Integer period) {
        commonService.refreshCommonValues(period);
        commonService.setDataToModel(model);
        hostRestrictionService.setDataToModel(model);
        podstationService.getCurrentPodstationFromNewPeriod(period);
        podstationService.setDataToModel(model);
        return commonService.getViewName();
    }

    @Override
    public String changePodstation(Model model, @RequestParam(value = "podstation") Integer rn) {
        if (commonService.getActivity().equals(Activity.INDEX)) {
            commonService.setActivity(Activity.SHOW_PODSTATION);
        }
        podstationService.updatePodstationByRn(rn);
        commonService.setDataToModel(model);
        hostRestrictionService.setDataToModel(model);
        podstationService.setDataToModel(model);
        return commonService.getViewName();
    }

    @Override
    public String searchPodstation() {
        return null;
    }
}
