package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.OperatingStatus;
import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.model.attraction.Attraction;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import com.jamesmhare.viewthequeue.service.AreaService;
import com.jamesmhare.viewthequeue.service.AttractionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

/**
 * A class containing Test Cases for the {@link AssociateController}.
 *
 * @author James Hare
 */
public class AssociateControllerTest {

    private final Long themeParkId = 1L;
    private final String themeParkName = "test theme park name";
    private final ThemePark themePark = ThemePark.builder().themeParkId(themeParkId).name(themeParkName).build();
    private final Long areaId = 1L;
    private final String areaName = "test area name";
    private final Area area = Area.builder().areaId(areaId).name(areaName).themePark(themePark).build();
    private final Long attractionId = 1L;
    private final String attractionName = "test attraction name";
    private final Attraction attraction = Attraction.builder().attractionId(attractionId).name(attractionName)
            .area(area).themePark(themePark).build();
    private AssociateController controller;

    @Mock
    private Model mockModel;
    @Mock
    private AreaService mockAreaService;
    @Mock
    private AttractionService mockAttractionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AssociateController(mockAttractionService, mockAreaService);
    }

    @Test
    public void testShowUpdateAttractionView() {
        Mockito.when(mockAttractionService.findAttractionById(attractionId)).thenReturn(Optional.of(attraction));
        final String actual = controller.showUpdateAttractionView(attractionId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("attractionToUpdate", attraction);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Assertions.assertEquals("/associate-views/update-attraction", actual);
    }

    @Test
    public void testShowUpdateAttractionView_AttractionNotFound() {
        Mockito.when(mockAttractionService.findAttractionById(attractionId)).thenReturn(Optional.empty());
        final String actual = controller.showUpdateAttractionView(attractionId, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Assertions.assertEquals("/associate-views/update-attraction", actual);
    }

    @Test
    public void testUpdateAttraction_Success() throws Exception {
        Mockito.when(mockAttractionService.findAttractionById(attractionId)).thenReturn(Optional.of(attraction));
        Mockito.when(mockAreaService.findAreaById(areaId)).thenReturn(Optional.of(area));
        Mockito.when(mockAttractionService.saveAttraction(attraction)).thenReturn(attraction);
        Mockito.when(mockAttractionService.findAllAttractionsByAreaId(areaId)).thenReturn(List.of(attraction));

        final String actual = controller.updateAttraction(attractionId, attraction, mockModel);

        Mockito.verify(mockAttractionService, Mockito.times(1)).saveAttraction(attraction);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(attractionName + " was updated."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("attractionToUpdate", attraction);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Assertions.assertEquals("/associate-views/update-attraction", actual);
    }

    @Test
    public void testUpdateAttraction_ExistingAreaNotFound() {
        Mockito.when(mockAttractionService.findAttractionById(attractionId)).thenReturn(Optional.empty()).thenReturn(Optional.of(attraction));
        final String actual = controller.updateAttraction(attractionId, attraction, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of("Attraction could not be updated."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("attractionToUpdate", attraction);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Assertions.assertEquals("/associate-views/update-attraction", actual);
    }

    @Test
    public void testUpdateAttraction_ExceptionDuringSave() throws Exception {
        Mockito.when(mockAttractionService.findAttractionById(attractionId)).thenReturn(Optional.of(attraction));
        Mockito.when(mockAttractionService.saveAttraction(attraction)).thenThrow(new Exception());
        Mockito.when(mockAttractionService.findAllAttractionsByAreaId(areaId)).thenReturn(List.of(attraction));

        final String actual = controller.updateAttraction(attractionId, attraction, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("An error occurred when trying to update " + attractionName + "."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("attractionToUpdate", attraction);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("operatingStatuses", OperatingStatus.values());
        Assertions.assertEquals("/associate-views/update-attraction", actual);
    }

}
