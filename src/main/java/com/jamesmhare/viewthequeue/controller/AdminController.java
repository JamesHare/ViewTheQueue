package com.jamesmhare.viewthequeue.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    public AdminController() { }

    @GetMapping("dashboard")
    public String showAdminDashboard(final Model model) {
        return "/admin/admin-dashboard";
    }

}