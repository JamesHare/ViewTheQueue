package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.service.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * The Area Controller that is available to all users with the USER role.
 *
 * @author James Hare
 */
@Slf4j
@Controller
@RequestMapping("areas")
public class AreaController {

    private final AreaService areaService;

    public AreaController(final AreaService areaService) {
        this.areaService = areaService;
    }

    /**
     * Sets model attributes and returns the User Area View for areas belonging to a specific Theme Park.
     *
     * @param themeParkId used to determine which areas should be included in the view.
     * @param model       the Spring Model.
     * @return the Areas Dashboard View.
     */
    @GetMapping("{theme-park-id}")
    public String showAreasDashboard(@PathVariable("theme-park-id") final Long themeParkId, final Model model) {
        final List<Area> allAreas = areaService.findAllAreasByThemeParkId(themeParkId);
        model.addAttribute("areas", allAreas);
        return "/user-views/view-areas";
    }

}
