package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.OperatingStatus;
import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.model.area.dto.AreaDto;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import com.jamesmhare.viewthequeue.model.themepark.dto.ThemeParkDto;
import com.jamesmhare.viewthequeue.service.AreaService;
import com.jamesmhare.viewthequeue.service.ThemeParkService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AdminControllerTest {

    private final List<ThemePark> allThemeParks = Collections.emptyList();
    private final Long themeParkId = 1L;
    private final String themeParkName = "test theme park name";
    private final ThemePark themePark = ThemePark.builder().themeParkId(themeParkId).name(themeParkName).build();
    private final List<Area> allAreas = Collections.emptyList();
    private final Long areaId = 1L;
    private final String areaName = "test area name";
    private final Area area = Area.builder().areaId(areaId).name(areaName).themePark(themePark).build();
    private AdminController controller;

    @Mock
    private Model mockModel;
    @Mock
    private ThemeParkService mockThemeParkService;
    @Mock
    private AreaService mockAreaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(mockThemeParkService.findAllThemeParks()).thenReturn(allThemeParks);
        Mockito.when(mockAreaService.findAllAreasByThemeParkId(themeParkId)).thenReturn(allAreas);
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.of(themePark));
        controller = new AdminController(mockThemeParkService, mockAreaService);
    }

    @Test
    public void testShowThemeParksDashboard() {
        final String actual = controller.showThemeParksDashboard(mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testAddThemePark_SavedSuccessfully() throws Exception {
        Mockito.when(mockThemeParkService.saveThemePark(themePark)).thenReturn(themePark);
        final String actual = controller.addThemePark(themePark, mockModel);

        Mockito.verify(mockThemeParkService, Mockito.times(1)).saveThemePark(themePark);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(themeParkName + " was added."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testAddThemePark_SaveThrowsException() throws Exception {
        Mockito.when(mockThemeParkService.saveThemePark(themePark)).thenThrow(new Exception());
        final String actual = controller.addThemePark(themePark, mockModel);

        Mockito.verify(mockThemeParkService, Mockito.times(1)).saveThemePark(themePark);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of("An error occurred when trying to add the Theme Park."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testShowUpdateThemeParkView() {
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.of(themePark));
        final String actual = controller.showUpdateThemeParkView(themeParkId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkToUpdate", themePark);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testShowUpdateThemeParkView_ThemeParkNotFound() {
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.empty());
        final String actual = controller.showUpdateThemeParkView(themeParkId, mockModel);

        Mockito.verify(mockModel, Mockito.times(0)).addAttribute("themeParkToUpdate", themePark);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testUpdateThemePark_Success() throws Exception {
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.of(themePark));
        Mockito.when(mockThemeParkService.saveThemePark(themePark)).thenReturn(themePark);
        Mockito.when(mockAreaService.findAllAreasByThemeParkId(themeParkId)).thenReturn(List.of(area));
        final String actual = controller.updateThemePark(themeParkId, themePark, mockModel);

        Mockito.verify(mockThemeParkService, Mockito.times(1)).saveThemePark(themePark);
        Mockito.verify(mockAreaService, Mockito.times(1)).saveAreas(List.of(area));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(themeParkName + " was updated."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testUpdateThemePark_ExistingThemeParkNotFound() {
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.empty());
        final String actual = controller.updateThemePark(themeParkId, themePark, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of("Theme Park could not be updated."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testUpdateThemePark_ExceptionDuringSave() throws Exception {
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.of(themePark));
        Mockito.doThrow(new Exception()).when(mockThemeParkService).saveThemePark(themePark);
        final String actual = controller.updateThemePark(themeParkId, themePark, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("An error occurred when trying to update " + themeParkName + "."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testShowDeleteThemeParkView() {
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.of(themePark));
        final String actual = controller.showDeleteThemeParkView(themeParkId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkToDelete", themePark);
        Assertions.assertEquals("/admin/confirm-delete-theme-park", actual);
    }

    @Test
    public void testShowDeleteThemeParkView_ParkNotFound() {
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.empty());
        final String actual = controller.showDeleteThemeParkView(themeParkId, mockModel);

        Mockito.verify(mockModel, Mockito.times(0)).addAttribute("themeParkToDelete", themePark);
        Assertions.assertEquals("/admin/confirm-delete-theme-park", actual);
    }

    @Test
    public void testDeleteThemePark_Success() throws Exception {
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.of(themePark));
        Mockito.when(mockAreaService.findAllAreasByThemeParkId(themeParkId)).thenReturn(List.of(area));
        final String actual = controller.deleteThemePark(themeParkId, themePark, mockModel);

        Mockito.verify(mockAreaService, Mockito.times(1)).deleteAreas(List.of(area));
        Mockito.verify(mockThemeParkService, Mockito.times(1)).deleteThemePark(themeParkId);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of(themeParkName + " was deleted."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testDeleteThemePark_ExistingThemeParkNotFound() {
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.empty());
        final String actual = controller.deleteThemePark(themeParkId, themePark, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of("Theme Park could not be deleted."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testDeleteThemePark_ExceptionDuringDelete() throws Exception {
        Mockito.when(mockThemeParkService.findThemeParkById(themeParkId)).thenReturn(Optional.of(themePark));
        Mockito.doThrow(new Exception()).when(mockThemeParkService).deleteThemePark(themeParkId);
        final String actual = controller.deleteThemePark(themeParkId, themePark, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("An error occurred when trying to delete " + themeParkName + "."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParks", allThemeParks);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("themeParkDto", ThemeParkDto.builder().build());
        Assertions.assertEquals("/admin/view-theme-parks", actual);
    }

    @Test
    public void testShowAreasDashboard() {
        final String actual = controller.showAreasDashboard(themeParkId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", allAreas);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().themePark(themePark).build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testShowAreasDashboard_WithNullThemeParkId() {
        Mockito.when(mockAreaService.findAllAreas()).thenReturn(allAreas);
        Mockito.when(mockThemeParkService.findThemeParkById(null)).thenReturn(Optional.empty());
        final String actual = controller.showAreasDashboard(null, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", allAreas);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testAddArea_SavedSuccessfully() throws Exception {
        final String actual = controller.addArea(area, mockModel);

        Mockito.verify(mockAreaService, Mockito.times(1)).saveArea(area);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(areaName + " was added."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", allAreas);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().themePark(themePark).build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testAddArea_SaveThrowsException() throws Exception {
        Mockito.when(mockAreaService.saveArea(area)).thenThrow(new Exception());
        final String actual = controller.addArea(area, mockModel);

        Mockito.verify(mockAreaService, Mockito.times(1)).saveArea(area);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of("An error occurred when trying to add the Area."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", allAreas);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().themePark(themePark).build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testShowUpdateAreaView() {
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.of(area));
        final String actual = controller.showUpdateAreaView(areaId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaToUpdate", area);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", allAreas);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().themePark(themePark).build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testShowUpdateAreaView_ParkNotFound() {
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.empty());
        Mockito.when(mockAreaService.findAllAreas()).thenReturn(allAreas);
        Mockito.when(mockThemeParkService.findThemeParkById(null)).thenReturn(Optional.empty());
        final String actual = controller.showUpdateAreaView(areaId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", allAreas);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testUpdateArea_Success() throws Exception {
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.of(area));
        Mockito.when(mockAreaService.saveArea(area)).thenReturn(area);
        Mockito.when(mockAreaService.findAllAreasByThemeParkId(themeParkId)).thenReturn(List.of(area));

        final String actual = controller.updateArea(areaId, area, mockModel);

        Mockito.verify(mockAreaService, Mockito.times(1)).saveArea(area);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(areaName + " was updated."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", List.of(area));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().themePark(themePark).build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testUpdateArea_ExistingAreaNotFound() {
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.empty());
        Mockito.when(mockAreaService.findAllAreasByThemeParkId(themeParkId)).thenReturn(List.of(area));

        final String actual = controller.updateArea(areaId, area, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of("Area could not be updated."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", List.of(area));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().themePark(themePark).build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testUpdateArea_ExceptionDuringSave() throws Exception {
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.of(area));
        Mockito.when(mockAreaService.saveArea(area)).thenThrow(new Exception());
        Mockito.when(mockAreaService.findAllAreasByThemeParkId(themeParkId)).thenReturn(List.of(area));

        final String actual = controller.updateArea(areaId, area, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("An error occurred when trying to update " + areaName + "."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", List.of(area));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().themePark(themePark).build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testShowDeleteAreaView() {
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.of(area));
        final String actual = controller.showDeleteAreaView(areaId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaToDelete", area);
        Assertions.assertEquals("/admin/confirm-delete-area", actual);
    }

    @Test
    public void testShowDeleteAreaView_AreaNotFound() {
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.empty());
        final String actual = controller.showDeleteAreaView(areaId, mockModel);

        Mockito.verify(mockModel, Mockito.times(0)).addAttribute("areaToDelete", area);
        Assertions.assertEquals("/admin/confirm-delete-area", actual);
    }

    @Test
    public void testDeleteArea_Success() throws Exception {
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.of(area));
        final String actual = controller.deleteArea(areaId, area, mockModel);

        Mockito.verify(mockAreaService, Mockito.times(1)).deleteArea(areaId);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of(areaName + " was deleted."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", allAreas);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().themePark(themePark).build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testDeleteArea_ExistingAreaNotFound() {
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.empty());
        final String actual = controller.deleteArea(areaId, area, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of("Area could not be deleted."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", allAreas);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().themePark(themePark).build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

    @Test
    public void testUpdateArea_ExceptionDuringDelete() throws Exception {
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.of(area));
        Mockito.doThrow(new Exception()).when(mockAreaService).deleteArea(areaId);
        final String actual = controller.deleteArea(areaId, area, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("An error occurred when trying to delete " + areaName + "."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areas", allAreas);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("areaDto", AreaDto.builder().themePark(themePark).build());
        Assertions.assertEquals("/admin/view-areas", actual);
    }

}
