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


    //GET method
    @Override
    public String editPodstationValues(Model model, @RequestParam(value = "message", required = false) String message,
                                       @RequestParam(value = "rn") int rn) {
        podstationService.podstationRnCheck(rn);
        if (message != null) {
            if (message.equals("saved")) {
                model.addAttribute("message", IMessageConstants.PODSTATION_SAVED);
            }
            if (message.equals("notsaved")) {
                model.addAttribute("message", IMessageConstants.PODSTATION_NOTNING_TO_SAVE);
            }
        }
        wrapData(model);
        return "editpodstationvalues";
    }

    //POST method
    @Override
    public String editPodstationValues(Model model, @ModelAttribute("currentPodstation") Podstation podstation,
                                       @RequestParam(value = "action") String action) {
        if (action.equals("save")) {
            String message;
            if (podstationService.updatePodstationValues(podstation)) {
                message = "saved";
            } else {
                message = "notsaved";
            }
            model.addAttribute("message", message);
        } else if (action.equals("addP")) {
            podstationService.createIntermediateMeasure(podstation);
        } else if (action.startsWith("pTransDel")) {
            Integer pTransformatorRn = Integer.valueOf(action.split("&")[1]);
            podstationService.deleteIntermediateTransformator(pTransformatorRn);
        } else {

        }
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/editpodstationvalues";
    }

    //GET method
    @Override
    public String editPodstationParams(Model model, @RequestParam(value = "message", required = false) String message,
                                       @RequestParam(value = "rn") int rn) {
        podstationService.podstationRnCheck(rn);
        wrapData(model);
        return "editpodstationparams";
    }

    //POST method
    @Override
    public String editPodstationParams(Model model, @ModelAttribute("currentPodstation") Podstation podstation,
                                       @RequestParam(value = "action") String action) {
        if (action.equals("save")) {
            podstationService.updatePodstationParams(podstation);
        } else if (action.equals("streetsedit")) {

        } else {
            String target = action.split("&")[0];
            String operation = action.split("&")[1];
            int rn = Integer.valueOf(action.split("&")[2]);
            if (target.equals("trans")) {
                if (operation.equals("add")) {
                    podstationService.addTransformator(podstation);
                } else if (operation.equals("del")) {
                    podstationService.deleteTransformator(rn);
                }
            } else if (target.equals("line")) {
                if (operation.equals("add")) {
                    podstationService.addLine(rn);
                } else if (operation.equals("up")) {
                    podstationService.moveLine(rn, "up");
                } else if (operation.equals("down")) {
                    podstationService.moveLine(rn, "down");
                } else if (operation.equals("del")) {
                    podstationService.deleteLine(rn);
                }
            }
        }
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        return "redirect:/editpodstationparams";
    }


}
