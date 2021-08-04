package ru.donenergo.journalr.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.donenergo.journalr.models.SystemParams;

public interface IAdminController {

    @GetMapping("/admin")
    String getAdminPage(Model model);

    @PostMapping("/admin")
    String saveSystemParams(Model model, @ModelAttribute("systemParams") SystemParams systemParams);

    @PostMapping("/hostsave")
    String hostSave(Model model, @ModelAttribute("systemParams") SystemParams systemParams);

    @PostMapping("/newperiod")
    String startNewPeriod(Model model);

}
