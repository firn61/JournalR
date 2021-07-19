package ru.donenergo.journalr.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

public interface IStartController {

    @GetMapping("/")
    String index(Model model, HttpServletRequest request);

}
