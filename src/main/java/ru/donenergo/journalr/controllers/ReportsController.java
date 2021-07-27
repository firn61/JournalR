package ru.donenergo.journalr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.services.CommonService;
import ru.donenergo.journalr.services.PodstationService;
import ru.donenergo.journalr.services.ReportsService;

@Controller
public class ReportsController {

    private final PodstationService podstationService;
    private final ReportsService reportsService;
    private final CommonService commonService;

    public ReportsController(PodstationService podstationService, ReportsService reportsService, CommonService commonService) {
        this.podstationService = podstationService;
        this.reportsService = reportsService;
        this.commonService = commonService;
    }

    @GetMapping("/measureblankreport")
    public String getMeasureBlankReport(Model model) {
        Podstation currentPodstation = podstationService.getCurrentPodstation();
        int transformatorsCount = currentPodstation.getTransformators().size();
        model.addAttribute("currentPodstation", currentPodstation);
        model.addAttribute("lines", reportsService.getBlankReportLines(currentPodstation));
        if (transformatorsCount == 2) {
            return "measureblank2";
        } else if (transformatorsCount == 1) {
            return "measureblank1";
        } else {
            return null;
        }
    }

    @GetMapping("/reportall")
    public String getReportAllPodstations(Model model){
        model.addAttribute("reportElements", reportsService.getReportAllPodstations(commonService.getCurrentPeriod()));
        return "reportall";
    }

    @GetMapping("/overloadreport")
    public String getOverloadPodstations(Model model){
        model.addAttribute("overloaded", reportsService.getOverloadedPodstations(commonService.getCurrentPeriod()));
        return "overloadreport";
    }

}
