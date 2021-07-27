package ru.donenergo.journalr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.donenergo.journalr.services.AdminService;

@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model){
        return null;
    }

    @PostMapping("/admin")
    public String saveSystemParams(Model model){
        return null;
    }

    @PostMapping("/hostsave")
    public String hostSave(Model model){
        return null;
    }

    @PostMapping("/newperiod")
    public String startNewPeriod(Model model){
        return null;
    }
}
