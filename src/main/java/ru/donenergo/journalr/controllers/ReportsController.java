package ru.donenergo.journalr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.donenergo.journalr.models.Podstation;
import ru.donenergo.journalr.services.CommonService;
import ru.donenergo.journalr.services.HostRestrictionService;
import ru.donenergo.journalr.services.PodstationService;
import ru.donenergo.journalr.services.ReportsService;

@Controller
public class ReportsController implements IReportsController{

    private final PodstationService podstationService;
    private final ReportsService reportsService;
    private final CommonService commonService;
    private final HostRestrictionService hostRestrictionService;

    public ReportsController(PodstationService podstationService, ReportsService reportsService,
                             CommonService commonService, HostRestrictionService hostRestrictionService) {
        this.podstationService = podstationService;
        this.reportsService = reportsService;
        this.commonService = commonService;
        this.hostRestrictionService = hostRestrictionService;
    }

    @Override
    public String getMeasureReport(Model model){
        Podstation currentPodstation = podstationService.getCurrentPodstation();
        model.addAttribute("currentPodstation", currentPodstation);
        model.addAttribute("res", hostRestrictionService.getResName());
        model.addAttribute("date", commonService.getPeriods().get(commonService.getCurrentPeriod()-1));
        model.addAttribute("measureTable", reportsService.getMeasureReport(currentPodstation));
        return "measurereport";
    }

    @Override
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

    @Override
    public String getReportAllPodstations(Model model){
        model.addAttribute("reportElements", reportsService.getReportAllPodstations(commonService.getCurrentPeriod()));
        return "reportall";
    }

    @Override
    public String getOverloadPodstations(Model model){
        model.addAttribute("overloaded", reportsService.getOverloadedPodstations(commonService.getCurrentPeriod()));
        return "overloadreport";
    }

}
