package com.jamesmhare.viewthequeue.service;

import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.model.repo.AreaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

public class AreaServiceTest {

    private final Long areaId = 1L;
    private final Long themeParkId = 1L;
    private final Area area = Area.builder().areaId(areaId).build();
    private AreaService areaService;

    @Mock
    private AreaRepository mockAreaRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        areaService = new AreaServiceImpl(mockAreaRepository);
    }

    @Test
    public void testFindAllAreas() {
        Mockito.when(mockAreaRepository.findAll()).thenReturn(List.of(area));
        List<Area> allAreas = areaService.findAllAreas();
        allAreas.forEach(foundArea -> Assertions.assertEquals(area, foundArea));
    }

    @Test
    public void testFindAllAreasByThemeParkId() {
        Mockito.when(mockAreaRepository.findAllByThemeParkThemeParkId(themeParkId)).thenReturn(List.of(area));
        List<Area> allAreas = areaService.findAllAreasByThemeParkId(themeParkId);
        allAreas.forEach(foundArea -> Assertions.assertEquals(area, foundArea));
    }

    @Test
    public void testFindAreaById() {
        Mockito.when(mockAreaRepository.findById(areaId)).thenReturn(Optional.ofNullable(area));
        Optional<Area> areaById = areaService.findAreaById(areaId);
        Assertions.assertEquals(area, areaById.get());
    }

    @Test
    public void testSaveArea() throws Exception {
        Mockito.when(mockAreaRepository.save(area)).thenReturn((area));
        Area savedArea = areaService.saveArea(area);
        Assertions.assertEquals(area, savedArea);
    }

    @Test
    public void testSaveAreas() throws Exception {
        Mockito.when(mockAreaRepository.saveAll(List.of(area))).thenReturn((List.of(area)));
        List<Area> savedAreas = areaService.saveAreas(List.of(area));
        Assertions.assertEquals(List.of(area), savedAreas);
    }

    @Test
    public void testDeleteArea() throws Exception {
        areaService.deleteArea(areaId);
        Mockito.verify(mockAreaRepository, Mockito.times(1)).deleteById(areaId);
    }

    @Test
    public void testDeleteAreas() throws Exception {
        areaService.deleteAreas(List.of(area));
        Mockito.verify(mockAreaRepository, Mockito.times(1)).deleteAll(List.of(area));
    }
    
}
