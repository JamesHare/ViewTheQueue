package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.OperatingStatus;
import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.model.area.dto.AreaDto;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import com.jamesmhare.viewthequeue.model.themepark.dto.ThemeParkDto;
import com.jamesmhare.viewthequeue.service.AreaService;
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
    private final AreaService areaService;

    public AdminController(final ThemeParkService themeParkService,
                           final AreaService areaService) {
        this.themeParkService = themeParkService;
        this.areaService = areaService;
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
                final List<Area> allAreasByThemeParkId = areaService.findAllAreasByThemeParkId(id);
                themePark.setThemeParkId(id);
                final ThemePark updatedThemePark = themeParkService.saveThemePark(themePark);
                for (final Area area : allAreasByThemeParkId) {
                    area.setThemePark(updatedThemePark);
                }
                areaService.saveAreas(allAreasByThemeParkId);
                model.addAttribute("success", true);
                model.addAttribute("messages", List.of(updatedThemePark.getName() + " was updated."));
            }
        } catch (final Exception e) {
            log.error("An error occurred when trying to update theme park = {}", themePark, e);
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
                final List<Area> allAreasByThemeParkId = areaService.findAllAreasByThemeParkId(id);
                areaService.deleteAreas(allAreasByThemeParkId);
                themeParkService.deleteThemePark(themePark.getThemeParkId());
                model.addAttribute("success", true);
                model.addAttribute("messages", List.of(themePark.getName() + " was deleted."));
            }
        } catch (final Exception e) {
            log.error("An error occurred when trying to delete theme park = {}", themePark, e);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("An error occurred when trying to delete " + themePark.getName() + "."));
        }
        return showThemeParksDashboard(model);
    }

    //-------------------- END THEME PARK CRUD --------------------//

    //-------------------- START AREA CRUD --------------------//

    @GetMapping("/areas/{theme-park-id}")
    public String showAreasDashboard(@PathVariable("theme-park-id") final Long themeParkId, final Model model) {
        List<Area> allAreas;
        if (themeParkId == null) {
            allAreas = areaService.findAllAreas();
        } else {
            allAreas = areaService.findAllAreasByThemeParkId(themeParkId);
        }
        Optional<ThemePark> themeParkById = themeParkService.findThemeParkById(themeParkId);
        AreaDto.AreaDtoBuilder areaBuilder = AreaDto.builder();
        themeParkById.ifPresent(areaBuilder::themePark);
        model.addAttribute("areas", allAreas);
        model.addAttribute("areaDto", areaBuilder.build());
        return "/admin/view-areas";
    }

    @PostMapping("/areas/add")
    public String addArea(final Area area, final Model model) {
        try {
            areaService.saveArea(area);
            model.addAttribute("success", true);
            model.addAttribute("messages", List.of(area.getName() + " was added."));
        } catch (final Exception e) {
            log.error("An error occurred when trying to add area = {}", area, e);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("An error occurred when trying to add the Area."));
        }
        return showAreasDashboard(area.getThemePark().getThemeParkId(), model);
    }

    @GetMapping("/areas/edit/{id}")
    public String showUpdateAreaView(@PathVariable("id") final Long id, final Model model) {
        final Optional<Area> areaById = areaService.findAreaById(id);
        Long themeParkId = null;
        if (areaById.isPresent()) {
            model.addAttribute("areaToUpdate", areaById.get());
            themeParkId = areaById.get().getThemePark().getThemeParkId();
        }
        return showAreasDashboard(themeParkId, model);
    }

    @PostMapping("/areas/edit/{id}")
    public String updateArea(@PathVariable("id") final Long id, final Area area, final Model model) {
        try {
            final Optional<Area> existingArea = areaService.findAreaById(id);
            if (existingArea.isEmpty()) {
                model.addAttribute("success", false);
                model.addAttribute("messages", List.of("Area could not be updated."));
            } else {
                final Area updatedArea = areaService.saveArea(area);
                model.addAttribute("success", true);
                model.addAttribute("messages", List.of(updatedArea.getName() + " was updated."));
            }
        } catch (final Exception e) {
            log.error("An error occurred when trying to update area = {}", area, e);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("An error occurred when trying to update " + area.getName() + "."));
        }
        return showAreasDashboard(area.getThemePark().getThemeParkId(), model);
    }

    @GetMapping("/areas/delete/{id}")
    public String showDeleteAreaView(@PathVariable("id") final Long id, final Model model) {
        final Optional<Area> areaById = areaService.findAreaById(id);
        areaById.ifPresent(area -> model.addAttribute("areaToDelete", area));
        return "/admin/confirm-delete-area";
    }

    @PostMapping("/areas/delete/{id}")
    public String deleteArea(@PathVariable("id") final Long id, final Area area, final Model model) {
        try {
            final Optional<Area> existingArea = areaService.findAreaById(id);
            if (existingArea.isEmpty()) {
                model.addAttribute("success", false);
                model.addAttribute("messages", List.of("Area could not be deleted."));
            } else {
                areaService.deleteArea(area.getAreaId());
                model.addAttribute("success", true);
                model.addAttribute("messages", List.of(area.getName() + " was deleted."));
            }
        } catch (final Exception e) {
            log.error("An error occurred when trying to delete area = {}", area, e);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("An error occurred when trying to delete " + area.getName() + "."));
        }
        return showAreasDashboard(area.getThemePark().getThemeParkId(), model);
    }

    //-------------------- END AREA CRUD --------------------//

}
