package com.jamesmhare.viewthequeue.service;

import com.jamesmhare.viewthequeue.model.attraction.Attraction;
import com.jamesmhare.viewthequeue.model.repo.AttractionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

/**
 * A class containing Test Cases for the {@link AttractionService}.
 *
 * @author James Hare
 */
public class AttractionServiceTest {

    private final Long attractionId = 1L;
    private final Long areaId = 1L;
    private final Long themeParkId = 1L;
    private final Attraction attraction = Attraction.builder().attractionId(attractionId).build();
    private AttractionService attractionService;

    @Mock
    private AttractionRepository mockAttractionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        attractionService = new AttractionServiceImpl(mockAttractionRepository);
    }

    @Test
    public void testFindAllAttractions() {
        Mockito.when(mockAttractionRepository.findAll()).thenReturn(List.of(attraction));
        final List<Attraction> allAttractions = attractionService.findAllAttractions();
        allAttractions.forEach(foundAttraction -> Assertions.assertEquals(attraction, foundAttraction));
    }

    @Test
    public void testFindAllAttractionsByAreaId() {
        Mockito.when(mockAttractionRepository.findAllByAreaAreaId(areaId)).thenReturn(List.of(attraction));
        final List<Attraction> allAttractions = attractionService.findAllAttractionsByAreaId(areaId);
        allAttractions.forEach(foundAttraction -> Assertions.assertEquals(attraction, foundAttraction));
    }

    @Test
    public void testFindAllAttractionsByThemeParkId() {
        Mockito.when(mockAttractionRepository.findAllByThemeParkThemeParkId(themeParkId)).thenReturn(List.of(attraction));
        final List<Attraction> allAttractions = attractionService.findAllAttractionsByThemeParkId(themeParkId);
        allAttractions.forEach(foundAttraction -> Assertions.assertEquals(attraction, foundAttraction));
    }

    @Test
    public void testFindAttractionById() {
        Mockito.when(mockAttractionRepository.findById(attractionId)).thenReturn(Optional.ofNullable(attraction));
        final Optional<Attraction> attractionById = attractionService.findAttractionById(attractionId);
        Assertions.assertEquals(attraction, attractionById.get());
    }

    @Test
    public void testSaveAttraction() throws Exception {
        Mockito.when(mockAttractionRepository.save(attraction)).thenReturn((attraction));
        final Attraction savedAttraction = attractionService.saveAttraction(attraction);
        Assertions.assertEquals(attraction, savedAttraction);
    }

    @Test
    public void testSaveAttractions() throws Exception {
        Mockito.when(mockAttractionRepository.saveAll(List.of(attraction))).thenReturn((List.of(attraction)));
        final List<Attraction> savedAttractions = attractionService.saveAttractions(List.of(attraction));
        Assertions.assertEquals(List.of(attraction), savedAttractions);
    }

    @Test
    public void testDeleteAttraction() throws Exception {
        attractionService.deleteAttraction(attractionId);
        Mockito.verify(mockAttractionRepository, Mockito.times(1)).deleteById(attractionId);
    }

    @Test
    public void testDeleteAttractions() throws Exception {
        attractionService.deleteAttractions(List.of(attraction));
        Mockito.verify(mockAttractionRepository, Mockito.times(1)).deleteAll(List.of(attraction));
    }

}
