package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.OperatingStatus;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import com.jamesmhare.viewthequeue.model.themepark.dto.ThemeParkDto;
import com.jamesmhare.viewthequeue.service.ThemeParkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    private final ThemeParkService themeParkService;

    public AdminController(final ThemeParkService themeParkService) {
        this.themeParkService = themeParkService;
    }

    //-------------------- START THEME PARK CRUD --------------------//

    @GetMapping("theme-parks")
    public String showThemeParksDashboard(final Model model) {
        final List<ThemePark> allThemeParks = themeParkService.findAllThemeParks();
        model.addAttribute("themeParks", allThemeParks);
        model.addAttribute("operatingStatuses", OperatingStatus.values());
        model.addAttribute("themeParkDto", ThemeParkDto.builder().build());
        return "/admin/view-theme-parks";
    }

    @PostMapping("/theme-parks/add")
    public String addThemePark(final ThemePark themePark, final Model model) {
        try {
            final ThemePark addedThemePark = themeParkService.saveThemePark(themePark);
            model.addAttribute("success", true);
            model.addAttribute("messages", List.of(addedThemePark.getName() + " was added."));
        } catch (final Exception e) {
            log.error("An error occurred when trying to add theme park = {}", themePark);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("An error occurred when trying to add the Theme Park."));
        }
        return showThemeParksDashboard(model);
    }

    @GetMapping("/theme-parks/edit/{id}")
    public String showUpdateThemeParkView(@PathVariable("id") final Long id, final Model model) {
        final Optional<ThemePark> themeParkById = themeParkService.findThemeParkById(id);
        themeParkById.ifPresent(themePark -> model.addAttribute("themeParkToUpdate", themePark));
        return showThemeParksDashboard(model);
    }

    @PostMapping("/theme-parks/edit/{id}")
    public String updateThemePark(@PathVariable("id") final Long id, final ThemePark themePark, final Model model) {
        try {
            final Optional<ThemePark> existingThemePark = themeParkService.findThemeParkById(id);
            if (existingThemePark.isEmpty()) {
                model.addAttribute("success", false);
                model.addAttribute("messages", List.of("Theme Park could not be updated."));
            } else {
                themePark.setThemeParkId(id);
                final ThemePark updatedThemePark = themeParkService.saveThemePark(themePark);
                model.addAttribute("success", true);
                model.addAttribute("messages", List.of(updatedThemePark.getName() + " was updated."));
            }
        } catch (final Exception e) {
            log.error("An error occurred when trying to update theme park = {}", themePark);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("An error occurred when trying to update " + themePark.getName() + "."));
        }
        return showThemeParksDashboard(model);
    }

    @GetMapping("/theme-parks/delete/{id}")
    public String showDeleteThemeParkView(@PathVariable("id") final Long id, final Model model) {
        final Optional<ThemePark> themeParkById = themeParkService.findThemeParkById(id);
        themeParkById.ifPresent(themePark -> model.addAttribute("themeParkToDelete", themePark));
        return "/admin/confirm-delete-theme-park";
    }

    @PostMapping("/theme-parks/delete/{id}")
    public String deleteThemePark(@PathVariable("id") final Long id, final ThemePark themePark, final Model model) {
        try {
            final Optional<ThemePark> existingThemePark = themeParkService.findThemeParkById(id);
            if (existingThemePark.isEmpty()) {
                model.addAttribute("success", false);
                model.addAttribute("messages", List.of("Theme Park could not be deleted."));
            } else {
                themeParkService.deleteThemePark(themePark.getThemeParkId());
                model.addAttribute("success", true);
                model.addAttribute("messages", List.of(themePark.getName() + " was deleted."));
            }
        } catch (final Exception e) {
            log.error("An error occurred when trying to delete theme park = {}", themePark);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("An error occurred when trying to delete " + themePark.getName() + "."));
        }
        return showThemeParksDashboard(model);
    }

    //-------------------- END THEME PARK CRUD --------------------//

}
