package ru.donenergo.journalr.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public interface IReportsController {

    @GetMapping("/measureblankreport")
    String getMeasureBlankReport(Model model);

    @GetMapping("/reportall")
    String getReportAllPodstations(Model model);

    @GetMapping("/overloadreport")
    String getOverloadPodstations(Model model);

    @GetMapping("/measurereport")
    String getMeasureReport(Model model);
}
