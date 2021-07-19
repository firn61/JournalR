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

    @Override
    public String changePeriod(Model model, @RequestParam(value = "period") Integer period) {
        commonService.refreshCommonValues(period);
        podstationService.getCurrentPodstationFromNewPeriod(period);
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/displaypodstation";
    }

    @GetMapping("/displaypodstation")
    public String displayPodstation(Model model, @RequestParam(value = "rn") Integer rn){
        commonService.setDataToModel(model);
        hostRestrictionService.setDataToModel(model);
        podstationService.setDataToModel(model);
        return commonService.getViewName();
    }

    @Override
    public String changePodstation(Model model, @RequestParam(value = "podstation") Integer rn) {
        if (commonService.getActivity().equals(Activity.INDEX)) {
            commonService.setActivity(Activity.SHOW_PODSTATION);
        }
        podstationService.updatePodstationByRn(rn);
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/displaypodstation";
    }

    @Override
    public String searchPodstation(Model model, @RequestParam(value = "podstType") String podstType,
                                   @RequestParam(value = "podstationNum") Integer podstationNum) {
        if (commonService.getActivity().equals(Activity.INDEX)) {
            commonService.setActivity(Activity.SHOW_PODSTATION);
        }
        podstationService.getPodstationByNumAndType(podstationNum, podstType, commonService.getCurrentPeriod());
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/displaypodstation";
    }

    public String switchEditPodstation(Model model){
        return "redirect:/";
    }
}
