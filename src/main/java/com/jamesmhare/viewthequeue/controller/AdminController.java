package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.OperatingStatus;
import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.model.area.dto.AreaDto;
import com.jamesmhare.viewthequeue.model.attraction.Attraction;
import com.jamesmhare.viewthequeue.model.attraction.dto.AttractionDto;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import com.jamesmhare.viewthequeue.model.themepark.dto.ThemeParkDto;
import com.jamesmhare.viewthequeue.service.AreaService;
import com.jamesmhare.viewthequeue.service.AttractionService;
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

/**
 * The Admin Controller is responsible for defining endpoints to enable CRUD functionality
 * for domain objects across the web application.
 *
 * @author James Hare
 */
@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    private final ThemeParkService themeParkService;
    private final AreaService areaService;
    private final AttractionService attractionService;

    public AdminController(final ThemeParkService themeParkService,
                           final AreaService areaService,
                           final AttractionService attractionService) {
        this.themeParkService = themeParkService;
        this.areaService = areaService;
        this.attractionService = attractionService;
    }

    //-------------------- START THEME PARK CRUD --------------------//

    /**
     * Sets model attributes and returns everything needed to show the
     * Theme Park Dashboard view.
     *
     * @param model the Spring Model.
     * @return the Theme Park Dashboard view.
     */
    @GetMapping("theme-parks")
    public String showThemeParksDashboard(final Model model) {
        final List<ThemePark> allThemeParks = themeParkService.findAllThemeParks();
        model.addAttribute("themeParks", allThemeParks);
        model.addAttribute("operatingStatuses", OperatingStatus.values());
        model.addAttribute("themeParkDto", ThemeParkDto.builder().build());
        return "/admin/view-theme-parks";
    }

    /**
     * Adds a new Theme Park.
     *
     * @param themePark the Theme Park to add.
     * @param model     The Spring Model.
     * @return the Theme Park Dashboard View.
     */
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

    /**
     * Sets model attributes and returns everything needed to show the
     * Theme Park Dashboard view with an Update Theme Park model object.
     *
     * @param id    the ID of the Theme Park to update.
     * @param model the Spring Model.
     * @return the Theme Park Dashboard View.
     */
    @GetMapping("/theme-parks/edit/{id}")
    public String showUpdateThemeParkView(@PathVariable("id") final Long id, final Model model) {
        final Optional<ThemePark> themeParkById = themeParkService.findThemeParkById(id);
        themeParkById.ifPresent(themePark -> model.addAttribute("themeParkToUpdate", themePark));
        return showThemeParksDashboard(model);
    }

    /**
     * Updates a Theme Park.
     *
     * @param id        the ID of the Theme Park to update.
     * @param themePark the Theme Park with updated attributes.
     * @param model     the Spring Model.
     * @return the Theme Park Dashboard View.
     */
    @PostMapping("/theme-parks/edit/{id}")
    public String updateThemePark(@PathVariable("id") final Long id, final ThemePark themePark, final Model model) {
        try {
            final Optional<ThemePark> existingThemePark = themeParkService.findThemeParkById(id);
            if (existingThemePark.isEmpty()) {
                model.addAttribute("success", false);
                model.addAttribute("messages", List.of("Theme Park could not be updated."));
            } else {
                final List<Area> allAreasByThemeParkId = areaService.findAllAreasByThemeParkId(id);
                final List<Attraction> allAttractionsByThemeParkId = attractionService.findAllAttractionsByThemeParkId(id);
                themePark.setThemeParkId(id);
                final ThemePark updatedThemePark = themeParkService.saveThemePark(themePark);
                for (final Area area : allAreasByThemeParkId) {
                    area.setThemePark(updatedThemePark);
                }
                for (final Attraction attraction : allAttractionsByThemeParkId) {
                    attraction.setThemePark(updatedThemePark);
                }
                areaService.saveAreas(allAreasByThemeParkId);
                attractionService.saveAttractions(allAttractionsByThemeParkId);
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

    /**
     * Sets model attributes and returns everything needed to show the
     * Delete Theme Park view.
     *
     * @param id    the ID of the Theme Park to delete.
     * @param model the Spring Model.
     * @return the Delete Theme Park View.
     */
    @GetMapping("/theme-parks/delete/{id}")
    public String showDeleteThemeParkView(@PathVariable("id") final Long id, final Model model) {
        final Optional<ThemePark> themeParkById = themeParkService.findThemeParkById(id);
        themeParkById.ifPresent(themePark -> model.addAttribute("themeParkToDelete", themePark));
        return "/admin/confirm-delete-theme-park";
    }

    /**
     * Deletes a Theme Park.
     *
     * @param id        the ID of the Theme Park to delete.
     * @param themePark the Theme Park to Delete.
     * @param model     the Spring Model.
     * @return the Theme Park Dashboard View.
     */
    @PostMapping("/theme-parks/delete/{id}")
    public String deleteThemePark(@PathVariable("id") final Long id, final ThemePark themePark, final Model model) {
        try {
            final Optional<ThemePark> existingThemePark = themeParkService.findThemeParkById(id);
            if (existingThemePark.isEmpty()) {
                model.addAttribute("success", false);
                model.addAttribute("messages", List.of("Theme Park could not be deleted."));
            } else {
                final List<Attraction> allAttractionsByThemeParkId = attractionService.findAllAttractionsByThemeParkId(id);
                attractionService.deleteAttractions(allAttractionsByThemeParkId);
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

    /**
     * Sets model attributes and returns everything needed to show the
     * Areas Dashboard view.
     *
     * @param themeParkId the ID of the Theme Park that the areas should belong to.
     * @param model       the Spring Model.
     * @return the Areas Dashboard View.
     */
    @GetMapping("/areas/{theme-park-id}")
    public String showAreasDashboard(@PathVariable("theme-park-id") final Long themeParkId, final Model model) {
        final List<Area> allAreas;
        if (themeParkId == null) {
            allAreas = areaService.findAllAreas();
        } else {
            allAreas = areaService.findAllAreasByThemeParkId(themeParkId);
        }
        final Optional<ThemePark> themeParkById = themeParkService.findThemeParkById(themeParkId);
        final AreaDto.AreaDtoBuilder areaBuilder = AreaDto.builder();
        themeParkById.ifPresent(areaBuilder::themePark);
        model.addAttribute("areas", allAreas);
        model.addAttribute("areaDto", areaBuilder.build());
        return "/admin/view-areas";
    }

    /**
     * Adds a new Area.
     *
     * @param area  the Area to add.
     * @param model the Spring Model.
     * @return the Areas Dashboard View.
     */
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

    /**
     * Sets model attributes and returns everything needed to show the
     * Area Dashboard view with an Update Area model object.
     *
     * @param id    the ID of the Area to update.
     * @param model the Spring Model.
     * @return the Areas Dashboard View.
     */
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

    /**
     * Updates an Area.
     *
     * @param id    the ID of the Area to update.
     * @param area  the Area with updated attributes.
     * @param model the Spring Model.
     * @return the Area Dashboard View.
     */
    @PostMapping("/areas/edit/{id}")
    public String updateArea(@PathVariable("id") final Long id, final Area area, final Model model) {
        try {
            final Optional<Area> existingArea = areaService.findAreaById(id);
            if (existingArea.isEmpty()) {
                model.addAttribute("success", false);
                model.addAttribute("messages", List.of("Area could not be updated."));
            } else {
                final List<Attraction> allAttractionsByAreaId = attractionService.findAllAttractionsByAreaId(id);
                area.setAreaId(id);
                final Area updatedArea = areaService.saveArea(area);
                for (final Attraction attraction : allAttractionsByAreaId) {
                    attraction.setArea(updatedArea);
                }
                attractionService.saveAttractions(allAttractionsByAreaId);
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

    /**
     * Sets model attributes and returns everything needed to show the
     * Delete Area view.
     *
     * @param id    the ID of the Area to delete.
     * @param model the Spring Model.
     * @return the Delete Area View.
     */
    @GetMapping("/areas/delete/{id}")
    public String showDeleteAreaView(@PathVariable("id") final Long id, final Model model) {
        final Optional<Area> areaById = areaService.findAreaById(id);
        areaById.ifPresent(area -> model.addAttribute("areaToDelete", area));
        return "/admin/confirm-delete-area";
    }

    /**
     * Deletes an Area.
     *
     * @param id    the ID of the Area to delete.
     * @param area  the Area to delete.
     * @param model the Spring Model.
     * @return the Area Dashboard View.
     */
    @PostMapping("/areas/delete/{id}")
    public String deleteArea(@PathVariable("id") final Long id, final Area area, final Model model) {
        try {
            final Optional<Area> existingArea = areaService.findAreaById(id);
            if (existingArea.isEmpty()) {
                model.addAttribute("success", false);
                model.addAttribute("messages", List.of("Area could not be deleted."));
            } else {
                final List<Attraction> allAttractionsByAreaId = attractionService.findAllAttractionsByAreaId(id);
                attractionService.deleteAttractions(allAttractionsByAreaId);
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

    //-------------------- START ATTRACTION CRUD --------------------//

    /**
     * Sets model attributes and returns everything needed to show the
     * Attractions Dashboard view.
     *
     * @param areaId the ID of the Area that the Attractions belong to.
     * @param model  the Spring Model.
     * @return the Attractions Dashboard View.
     */
    @GetMapping("/attractions/{area-id}")
    public String showAttractionsDashboard(@PathVariable("area-id") final Long areaId, final Model model) {
        final List<Attraction> allAttractions = attractionService.findAllAttractionsByAreaId(areaId);
        final Optional<Area> areaById = areaService.findAreaById(areaId);
        final AttractionDto.AttractionDtoBuilder attractionBuilder = AttractionDto.builder();
        areaById.ifPresent(attractionBuilder::area);
        areaById.ifPresent(area -> attractionBuilder.themePark(area.getThemePark()));
        model.addAttribute("attractions", allAttractions);
        model.addAttribute("operatingStatuses", OperatingStatus.values());
        model.addAttribute("attractionDto", attractionBuilder.build());
        return "/admin/view-attractions";
    }

    /**
     * Adds a new Attraction.
     *
     * @param attraction the Attraction to add.
     * @param model      the Spring Model.
     * @return the Attractions Dashboard View.
     */
    @PostMapping("/attractions/add")
    public String addAttraction(final Attraction attraction, final Model model) {
        try {
            final Optional<Area> areaById = areaService.findAreaById(attraction.getArea().getAreaId());
            areaById.ifPresent(attraction::setArea);
            attractionService.saveAttraction(attraction);
            model.addAttribute("success", true);
            model.addAttribute("messages", List.of(attraction.getName() + " was added."));
        } catch (final Exception e) {
            log.error("An error occurred when trying to add attraction = {}", attraction, e);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("An error occurred when trying to add the Attraction."));
        }
        return showAttractionsDashboard(attraction.getArea().getAreaId(), model);
    }

    /**
     * Sets model attributes and returns everything needed to show the
     * Attractions Dashboard view with an Update Attraction model object.
     *
     * @param id    the ID of the attraction to update.
     * @param model the Spring Model.
     * @return the Attractions Dashboard View.
     */
    @GetMapping("/attractions/edit/{id}")
    public String showUpdateAttractionView(@PathVariable("id") final Long id, final Model model) {
        final Optional<Attraction> attractionById = attractionService.findAttractionById(id);
        Long areaId = null;
        if (attractionById.isPresent()) {
            model.addAttribute("attractionToUpdate", attractionById.get());
            areaId = attractionById.get().getArea().getAreaId();
        }
        return showAttractionsDashboard(areaId, model);
    }

    /**
     * Updates an Attraction.
     *
     * @param id         the ID of the Attraction to Update.
     * @param attraction the Attraction with updated attributes.
     * @param model      the Spring Model.
     * @return the Attractions Dashboard View.
     */
    @PostMapping("/attractions/edit/{id}")
    public String updateAttraction(@PathVariable("id") final Long id, final Attraction attraction, final Model model) {
        try {
            final Optional<Attraction> existingAttraction = attractionService.findAttractionById(id);
            if (existingAttraction.isEmpty()) {
                model.addAttribute("success", false);
                model.addAttribute("messages", List.of("Attraction could not be updated."));
            } else {
                final Optional<Area> areaById = areaService.findAreaById(attraction.getArea().getAreaId());
                areaById.ifPresent(attraction::setArea);
                final Attraction updatedAttraction = attractionService.saveAttraction(attraction);
                model.addAttribute("success", true);
                model.addAttribute("messages", List.of(updatedAttraction.getName() + " was updated."));
            }
        } catch (final Exception e) {
            log.error("An error occurred when trying to update attraction = {}", attraction, e);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("An error occurred when trying to update " + attraction.getName() + "."));
        }
        return showAttractionsDashboard(attraction.getArea().getAreaId(), model);
    }

    /**
     * Sets model attributes and returns everything needed to show the
     * Delete Attraction view.
     *
     * @param id    the ID of the Attracgtion to delete.
     * @param model the Spring Model.
     * @return the Delete Attraction View.
     */
    @GetMapping("/attractions/delete/{id}")
    public String showDeleteAttractionView(@PathVariable("id") final Long id, final Model model) {
        final Optional<Attraction> attractionById = attractionService.findAttractionById(id);
        attractionById.ifPresent(attraction -> model.addAttribute("attractionToDelete", attraction));
        return "/admin/confirm-delete-attraction";
    }

    /**
     * Deletes an Attraction.
     *
     * @param id         the ID of an Attraction to delete.
     * @param attraction the Attraction to delete.
     * @param model      the Spring Model.
     * @return the Attraction Dashboard View.
     */
    @PostMapping("/attractions/delete/{id}")
    public String deleteAttraction(@PathVariable("id") final Long id, final Attraction attraction, final Model model) {
        try {
            final Optional<Attraction> existingAttraction = attractionService.findAttractionById(id);
            if (existingAttraction.isEmpty()) {
                model.addAttribute("success", false);
                model.addAttribute("messages", List.of("Attraction could not be deleted."));
            } else {
                attractionService.deleteAttraction(attraction.getAttractionId());
                model.addAttribute("success", true);
                model.addAttribute("messages", List.of(attraction.getName() + " was deleted."));
            }
        } catch (final Exception e) {
            log.error("An error occurred when trying to delete attraction = {}", attraction, e);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("An error occurred when trying to delete " + attraction.getName() + "."));
        }
        return showAttractionsDashboard(attraction.getArea().getAreaId(), model);
    }

    //-------------------- END ATTRACTION CRUD --------------------//

}
