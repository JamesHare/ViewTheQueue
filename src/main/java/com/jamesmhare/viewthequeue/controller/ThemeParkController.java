package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import com.jamesmhare.viewthequeue.service.ThemeParkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("theme-parks")
public class ThemeParkController {

    private final ThemeParkService themeParkService;

    public ThemeParkController(final ThemeParkService themeParkService) {
        this.themeParkService = themeParkService;
    }

    @GetMapping()
    public String showThemeParksDashboard(final Model model) {
        final List<ThemePark> allThemeParks = themeParkService.findAllThemeParks();
        model.addAttribute("themeParks", allThemeParks);
        return "/user-views/view-theme-parks";
    }

}
