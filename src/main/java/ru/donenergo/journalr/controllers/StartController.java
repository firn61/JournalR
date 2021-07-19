package ru.donenergo.journalr.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.services.CommonService;
import ru.donenergo.journalr.services.HostRestrictionService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class StartController implements IStartController {

    private final CommonService commonService;
    private final HostRestrictionService hostRestrictionService;
    static final Logger logger = LoggerFactory.getLogger(StartController.class);

    @Autowired
    public StartController(CommonService commonService, HostRestrictionService hostRestrictionService) {
        this.commonService = commonService;
        this.hostRestrictionService = hostRestrictionService;
    }

    @Override
    public String index(Model model, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        logger.info("ipAddress: {}", ipAddress);
        hostRestrictionService.invokeValues(ipAddress);
        commonService.setDataToModel(model);
        model.addAttribute("currentPodstation", new Podstation(0,0, ""));
        hostRestrictionService.setDataToModel(model);
        return "index";
    }
}
