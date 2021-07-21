package ru.donenergo.journalr.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.donenergo.journalr.models.Podstation;

public interface IPodstationController {

    @GetMapping("/showpodstation")
    String showPodstation(Model model, int rn);

    @GetMapping("/editpodstationvalues")
    String editPodstationValues(Model model, String message, int rn);

    @PostMapping("/editpodstationvalues")
    String editPodstationValues(Model model, Podstation podstation, String action);

    @GetMapping("/editpodstationparams")
    String editPodstationParams(Model model, String message, int rn);

    @PostMapping("/editpodstationparams")
    String editPodstationParams(Model model, Podstation podstation, String action);

}
