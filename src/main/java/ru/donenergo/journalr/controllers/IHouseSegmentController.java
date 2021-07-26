package ru.donenergo.journalr.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.donenergo.journalr.models.HouseSegment;

public interface IHouseSegmentController {

    @GetMapping("/showhousesegment")
    String showHouseSegment(Model model, String streetName, Integer houseNum);

    @GetMapping("/edithousesegment")
    String editHouseSegment(Model model, int rn);

    @PostMapping("/edithousesegment")
    String editHouseSegment(Model model, String street, HouseSegment newHouseSegment, Integer transNum, String action);

}
