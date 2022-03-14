package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.attraction.Attraction;
import com.jamesmhare.viewthequeue.service.AttractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * The Attraction Controller that is available to all users with the USER role.
 *
 * @author James Hare
 */
@Slf4j
@Controller
@RequestMapping("attractions")
public class AttractionController {

    private final AttractionService attractionService;

    public AttractionController(final AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    /**
     * Sets the model attributes and returns the Attraction Dashboard View with Attractions
     * related to a specific Area.
     *
     * @param areaId the ID of an Area.
     * @param model  the Spring Model.
     * @return the Attraction Dashboard View.
     */
    @GetMapping("{area-id}")
    public String showAttractionsDashboard(@PathVariable("area-id") final Long areaId, final Model model) {
        final List<Attraction> allAttractions = attractionService.findAllAttractionsByAreaId(areaId);
        model.addAttribute("attractions", allAttractions);
        return "/user-views/view-attractions";
    }

    /**
     * Sets the model attributes and returns the Attraction Dashboard View with Attractions
     * related to a specific Area.
     *
     * @param themeParkId the ID of a Theme Park.
     * @param model       the Spring Model.
     * @return the Attraction Dashboard View.
     */
    @GetMapping("/by-park/{theme-park-id}")
    public String showAttractionsDashboardByPark(@PathVariable("theme-park-id") final Long themeParkId, final Model model) {
        final List<Attraction> allAttractions = attractionService.findAllAttractionsByThemeParkId(themeParkId);
        model.addAttribute("attractions", allAttractions);
        return "/user-views/view-attractions";
    }

}
