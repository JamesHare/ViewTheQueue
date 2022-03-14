package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.attraction.Attraction;
import com.jamesmhare.viewthequeue.service.AttractionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;

/**
 * A class containing Test Cases for the {@link AttractionController}.
 *
 * @author James Hare
 */
public class AttractionControllerTest {

    private final Long attractionId = 1L;
    private final Long areaId = 1L;
    private final Long themeParkId = 1L;
    private final String attractionName = "test name";
    private final Attraction attraction = Attraction.builder().attractionId(attractionId).name(attractionName).build();
    private final List<Attraction> allAttractions = List.of(attraction);
    private AttractionController controller;

    @Mock
    private Model mockModel;
    @Mock
    private AttractionService mockAttractionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AttractionController(mockAttractionService);
    }

    @Test
    public void testShowAttractionsDashboard() {
        Mockito.when(mockAttractionService.findAllAttractionsByAreaId(areaId)).thenReturn(allAttractions);
        final String actual = controller.showAttractionsDashboard(areaId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("attractions", allAttractions);
        Assertions.assertEquals("/user-views/view-attractions", actual);
    }

    @Test
    public void testShowAttractionsDashboardByPark() {
        Mockito.when(mockAttractionService.findAllAttractionsByThemeParkId(themeParkId)).thenReturn(allAttractions);
        final String actual = controller.showAttractionsDashboardByPark(themeParkId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("attractions", allAttractions);
        Assertions.assertEquals("/user-views/view-attractions", actual);
    }

}
