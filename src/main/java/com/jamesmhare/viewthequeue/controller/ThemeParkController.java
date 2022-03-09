package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import com.jamesmhare.viewthequeue.service.ThemeParkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * The Theme Park Controller that is available to all users with the USER role.
 *
 * @author James Hare
 */
@Slf4j
@Controller
@RequestMapping("theme-parks")
public class ThemeParkController {

    private final ThemeParkService themeParkService;

    public ThemeParkController(final ThemeParkService themeParkService) {
        this.themeParkService = themeParkService;
    }

    /**
     * Sets the model attributes and returns the Theme Park View showing all
     * Theme Parks available in the web application.
     *
     * @param model the Spring Model.
     * @return the Theme Park Dashboard View.
     */
    @GetMapping()
    public String showThemeParksDashboard(final Model model) {
        final List<ThemePark> allThemeParks = themeParkService.findAllThemeParks();
        model.addAttribute("themeParks", allThemeParks);
        return "/user-views/view-theme-parks";
    }

}
