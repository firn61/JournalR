package ru.donenergo.journalr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.donenergo.journalr.models.SystemParams;
import ru.donenergo.journalr.services.AdminService;

@Controller
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model){
        model.addAttribute("systemParams", adminService.getSystemParams());
        return "admin";
    }

    @PostMapping("/admin")
    public String saveSystemParams(Model model, @ModelAttribute("systemParams") SystemParams systemParams){
        adminService.updateSystemParams(systemParams);
        model.addAttribute("systemParams", adminService.getSystemParams());
        return "admin";
    }

    @PostMapping("/hostsave")
    public String hostSave(Model model, @ModelAttribute("systemParams") SystemParams systemParams){
        adminService.updateHosts(systemParams);
        model.addAttribute("systemParams", adminService.getSystemParams());
        return "admin";
    }

    @PostMapping("/newperiod")
    public String startNewPeriod(Model model){
        return null;
    }
}
