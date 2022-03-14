package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.OperatingStatus;
import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.model.attraction.Attraction;
import com.jamesmhare.viewthequeue.service.AreaService;
import com.jamesmhare.viewthequeue.service.AttractionService;
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
 * The Associate Controller is responsible for defining endpoints to enable update functionality
 * for attraction wait times and operating status's.
 *
 * @author James Hare
 */
@Slf4j
@Controller
@RequestMapping("associate")
public class AssociateController {

    private final AttractionService attractionService;
    private final AreaService areaService;

    public AssociateController(final AttractionService attractionService,
                               final AreaService areaService) {
        this.attractionService = attractionService;
        this.areaService = areaService;
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
        attractionById.ifPresent(attraction -> model.addAttribute("attractionToUpdate", attraction));
        model.addAttribute("operatingStatuses", OperatingStatus.values());
        return "/associate-views/update-attraction";
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
        return showUpdateAttractionView(attraction.getArea().getAreaId(), model);
    }

}
