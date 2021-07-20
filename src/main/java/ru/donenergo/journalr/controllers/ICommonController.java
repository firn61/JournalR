package ru.donenergo.journalr.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

public interface ICommonController {

    @GetMapping("/")
    String index(Model model, HttpServletRequest request);

    @GetMapping("/changeperiod")
    String changePeriod(Model model, int period);

    @GetMapping("/changepodstation")
    String changePodstation(Model model, int rn);

    @GetMapping("/searchpodstation")
    String searchPodstation(Model model, String podstType, Integer podstationNum);

}
