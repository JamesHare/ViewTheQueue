package com.jamesmhare.viewthequeue.service;

import com.jamesmhare.viewthequeue.model.repo.ThemeParkRepository;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

public class ThemeParkServiceTest {

    private final Long themeParkId = 1L;
    private final ThemePark themePark = ThemePark.builder().themeParkId(themeParkId).build();
    private ThemeParkService themeParkService;

    @Mock
    private ThemeParkRepository mockThemeParkRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        themeParkService = new ThemeParkServiceImpl(mockThemeParkRepository);
    }

    @Test
    public void testFindAllThemeParks() {
        Mockito.when(mockThemeParkRepository.findAll()).thenReturn(List.of(themePark));
        List<ThemePark> allThemeParks = themeParkService.findAllThemeParks();
        allThemeParks.forEach(foundThemePark -> Assertions.assertEquals(themePark, foundThemePark));
    }

    @Test
    public void testFindThemeParkById() {
        Mockito.when(mockThemeParkRepository.findById(themeParkId)).thenReturn(Optional.ofNullable(themePark));
        Optional<ThemePark> themeParkById = themeParkService.findThemeParkById(themeParkId);
        Assertions.assertEquals(themePark, themeParkById.get());
    }

    @Test
    public void testSaveThemePark() throws Exception {
        Mockito.when(mockThemeParkRepository.save(themePark)).thenReturn((themePark));
        ThemePark savedThemePark = themeParkService.saveThemePark(themePark);
        Assertions.assertEquals(themePark, savedThemePark);
    }

    @Test
    public void testDeleteThemePark() throws Exception {
        themeParkService.deleteThemePark(themeParkId);
        Mockito.verify(mockThemeParkRepository, Mockito.times(1)).deleteById(themeParkId);
    }

}
