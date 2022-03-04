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

@Slf4j
@Controller
@RequestMapping("areas")
public class AreaController {

    private final AreaService areaService;

    public AreaController(final AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("{theme-park-id}")
    public String showAreasDashboard(@PathVariable("theme-park-id") final Long themeParkId, final Model model) {
        List<Area> allAreas = areaService.findAllAreasByThemeParkId(themeParkId);
        model.addAttribute("areas", allAreas);
        return "/user-views/view-areas";
    }

}
