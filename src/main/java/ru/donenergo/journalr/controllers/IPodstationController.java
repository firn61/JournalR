package ru.donenergo.journalr.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public interface IPodstationController {

    @GetMapping("/changeperiod")
    String changePeriod(Model model, Integer period);

    @GetMapping("/changepodstation")
    String changePodstation(Model model, Integer rn);

    @GetMapping("/searchpodstation")
    String searchPodstation(Model model, String podstType, Integer podstationNum);

}
