package ru.donenergo.journalr.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.donenergo.journalr.exceptions.PodstationAlreadyExistException;
import ru.donenergo.journalr.models.BasicPodstation;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.services.*;

@Controller
public class PodstationController implements IPodstationController, IDataWrapper {

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
    public void wrapData(Model model) {
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


    //GET method
    @Override
    public String editPodstationValues(Model model, @RequestParam(value = "rn") int rn) {
        podstationService.podstationRnCheck(rn);
        wrapData(model);
        return "editpodstationvalues";
    }

    //POST method
    @Override
    public String editPodstationValues(Model model, @ModelAttribute("currentPodstation") Podstation podstation,
                                       @RequestParam(value = "action") String action) {
        if (action.equals("save")) {
            commonService.addMessage(podstationService.updatePodstationValues(podstation));
        } else if (action.equals("addP")) {
            commonService.addMessage(podstationService.createIntermediateMeasure(podstation));
        } else if (action.startsWith("pTransDel")) {
            Integer pTransformatorRn = Integer.valueOf(action.split("&")[1]);
            commonService.addMessage(podstationService.deleteIntermediateTransformator(pTransformatorRn));
        }
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/editpodstationvalues";
    }

    //GET method
    @Override
    public String editPodstationParams(Model model, @RequestParam(value = "rn") int rn) {
        podstationService.podstationRnCheck(rn);
        wrapData(model);
        return "editpodstationparams";
    }

    //POST method
    @Override
    public String editPodstationParams(Model model, @ModelAttribute("currentPodstation") Podstation podstation,
                                       @RequestParam(value = "action") String action) {
        if (action.equals("save")) {
            commonService.addMessage(podstationService.updatePodstationParams(podstation));
        } else if (action.equals("edithousesegment")) {
            commonService.setActivity(Activity.EDIT_HOUSE_SEGMENT);
            model.addAttribute("rn", podstationService.getCurrentPodstationRn());
            return "redirect:/edithousesegment";
        } else {
            String target = action.split("&")[0];
            String operation = action.split("&")[1];
            int rn = Integer.valueOf(action.split("&")[2]);
            if (target.equals("trans")) {
                if (operation.equals("add")) {
                    commonService.addMessage(podstationService.addTransformator(podstation));
                } else if (operation.equals("del")) {
                    commonService.addMessage(podstationService.deleteTransformator(rn));
                }
            } else if (target.equals("line")) {
                if (operation.equals("add")) {
                    commonService.addMessage(podstationService.addLine(rn));
                } else if (operation.equals("up")) {
                    commonService.addMessage(podstationService.moveLine(rn, "up"));
                } else if (operation.equals("down")) {
                    commonService.addMessage(podstationService.moveLine(rn, "down"));
                } else if (operation.equals("del")) {
                    commonService.addMessage(podstationService.deleteLine(rn));
                }
            }
        }
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/editpodstationparams";
    }

    @Override
    public String addPodstation(Model model, @RequestParam(value = "podstType") String podstType,
                                @RequestParam(value = "num") int num,
                                @RequestParam(value = "address") String address) {
        try {
            BasicPodstation basicPodstation = podstationService.addPodstation(podstType, num, address,
                    commonService.getCurrentPeriod(), hostRestrictionService.getHostResNum());
            commonService.addBasicPodstatinLabel(basicPodstation);
        } catch (PodstationAlreadyExistException e) {
            commonService.addError(e.getMessage());
            logger.info(e.getMessage());
        }
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/editpodstationparams";
    }


}
