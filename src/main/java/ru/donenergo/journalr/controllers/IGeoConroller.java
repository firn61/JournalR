package ru.donenergo.journalr.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public interface IGeoConroller {

    @GetMapping("/map")
    String getMap(Model mode, int rn);
}
