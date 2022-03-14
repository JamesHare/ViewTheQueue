package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import com.jamesmhare.viewthequeue.service.ThemeParkService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;

/**
 * A class containing Test Cases for the {@link ThemeParkController}.
 *
 * @author James Hare
 */
public class ThemeParkControllerTest {

    private final Long themeParkId = 1L;
    private final String themeParkName = "test name";
    private final ThemePark themePark = ThemePark.builder().themeParkId(themeParkId).name(themeParkName).build();
    private final List<ThemePark> allThemeParks = List.of(themePark);
    private ThemeParkController controller;

    @Mock
    private Model mockModel;
    @Mock
    private ThemeParkService mockThemeParkService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ThemeParkController(mockThemeParkService);
    }

    @Test
    public void testShowThemeParksDashboard() {
        Mockito.when(mockThemeParkService.findAllThemeParks()).thenReturn(allThemeParks);
        final String actual = controller.showThemeParksDashboard(mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Assertions.assertEquals("/user-views/view-theme-parks", actual);
    }

}
