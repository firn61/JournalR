package ru.donenergo.journalr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.donenergo.journalr.models.SystemParams;
import ru.donenergo.journalr.services.AdminService;
import ru.donenergo.journalr.services.CommonService;

@Controller
public class AdminController implements IAdminController{

    private final AdminService adminService;
    private final CommonService commonService;

    @Autowired
    public AdminController(AdminService adminService, CommonService commonService) {
        this.adminService = adminService;
        this.commonService = commonService;
    }

    @Override
    public String getAdminPage(Model model){
        model.addAttribute("systemParams", adminService.getSystemParams());
        return "admin";
    }

    @Override
    public String saveSystemParams(Model model, @ModelAttribute("systemParams") SystemParams systemParams){
        adminService.updateSystemParams(systemParams);
        model.addAttribute("systemParams", adminService.getSystemParams());
        return "admin";
    }

    @Override
    public String hostSave(Model model, @ModelAttribute("systemParams") SystemParams systemParams){
        adminService.updateHosts(systemParams);
        model.addAttribute("systemParams", adminService.getSystemParams());
        return "admin";
    }

    @Override
    public String startNewPeriod(Model model){
        adminService.startNewPeriod();
        commonService.invokeValues();
        model.addAttribute("systemParams", adminService.getSystemParams());
        return "admin";
    }
}
