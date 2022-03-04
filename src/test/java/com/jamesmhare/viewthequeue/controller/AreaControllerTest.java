package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.service.AreaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;

public class AreaControllerTest {

    private final Long areaId = 1L;
    private final Long themeParkId = 1L;
    private final String areaName = "test name";
    private final Area area = Area.builder().areaId(areaId).name(areaName).build();
    private final List<Area> allAreas = List.of(area);
    private AreaController controller;

    @Mock
    private Model mockModel;
    @Mock
    private AreaService mockAreaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AreaController(mockAreaService);
    }

    @Test
    public void testShowAreasDashboard() {
        Mockito.when(mockAreaService.findAllAreasByThemeParkId(themeParkId)).thenReturn(allAreas);
        final String actual = controller.showAreasDashboard(themeParkId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", allAreas);
        Assertions.assertEquals("/user-views/view-areas", actual);
    }

}
