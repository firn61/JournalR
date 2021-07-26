package ru.donenergo.journalr.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.services.Activity;
import ru.donenergo.journalr.services.CommonService;
import ru.donenergo.journalr.services.HostRestrictionService;
import ru.donenergo.journalr.services.PodstationService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommonController implements ICommonController {

    private final CommonService commonService;
    private final HostRestrictionService hostRestrictionService;
    private final PodstationService podstationService;
    static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    public CommonController(CommonService commonService, HostRestrictionService hostRestrictionService, PodstationService podstationService) {
        this.commonService = commonService;
        this.hostRestrictionService = hostRestrictionService;
        this.podstationService = podstationService;
    }

    @Override
    public String index(Model model, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        logger.info("ipAddress: {}", ipAddress);
        hostRestrictionService.invokeValues(ipAddress);
        commonService.setDataToModel(model);
        model.addAttribute("currentPodstation", new Podstation(0, 0, ""));
        hostRestrictionService.setDataToModel(model);
        return "index";
    }

    @GetMapping("/sweditpodstationparams")
    public String switchEditPodstationParams(Model model) {
        commonService.setActivity(Activity.EDIT_PODSTATION_PARAMS);
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/" + commonService.getViewName(podstationService.getCurrentPodstationRn());
    }

    @GetMapping("/swshowpodstation")
    public String switchShowPodstation(Model model) {
        commonService.setActivity(Activity.SHOW_PODSTATION);
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/" + commonService.getViewName(podstationService.getCurrentPodstationRn());
    }

    @GetMapping("/sweditpodstationvalues")
    public String switchEditPodstationValues(Model model) {
        commonService.setActivity(Activity.EDIT_PODSTATION_VALUES);
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/" + commonService.getViewName(podstationService.getCurrentPodstationRn());
    }

    @GetMapping("/swshowhousesegment")
    public String switchShowHouseSegment(Model model){
        commonService.setActivity(Activity.SHOW_HOUSE_SEGMENT);
        return "redirect:/" + commonService.getViewName(podstationService.getCurrentPodstationRn());
    }

    @Override
    public String changePeriod(Model model, @RequestParam(value = "period") int period) {
        commonService.refreshCommonValues(period);
        commonService.addError(podstationService.getCurrentPodstationFromNewPeriod(period));
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/" + commonService.getViewName(podstationService.getCurrentPodstationRn());
    }

    @Override
    public String changePodstation(Model model, @RequestParam(value = "podstation") int rn) {
        commonService.changeViewIfIndex();
        podstationService.updatePodstationByRn(rn);
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/" + commonService.getViewName(podstationService.getCurrentPodstationRn());
    }

    @Override
    public String searchPodstation(Model model, @RequestParam(value = "podstType") String podstType,
                                   @RequestParam(value = "podstationNum") Integer podstationNum) {
        commonService.changeViewIfIndex();
        if (podstationNum != null) {
            commonService.addError(podstationService.getPodstationByNumAndType(podstationNum, podstType, commonService.getCurrentPeriod()));
        }
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/" + commonService.getViewName(podstationService.getCurrentPodstationRn());
    }

}
