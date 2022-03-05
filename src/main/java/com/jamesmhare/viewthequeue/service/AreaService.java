package com.jamesmhare.viewthequeue.service;

import com.jamesmhare.viewthequeue.model.area.Area;

import java.util.List;
import java.util.Optional;

public interface AreaService {

    /**
     * Finds and returns all the areas in the datasource.
     *
     * @return a list of areas.
     */
    List<Area> findAllAreas();

    /**
     * Finds and returns all the areas of a theme park in the datasource.
     *
     * @param themeParkId a theme park id.
     * @return a list of areas in the theme park.
     */
    List<Area> findAllAreasByThemeParkId(final Long themeParkId);

    /**
     * Finds and returns an area matching a given id.
     *
     * @param id an area id.
     * @return an Optional containing either the area or empty.
     */
    Optional<Area> findAreaById(final Long id);

    /**
     * Saves an area to the datasource.
     *
     * @param area an area.
     * @return the saved area.
     * @throws Exception if area could not be saved.
     */
    Area saveArea(final Area area) throws Exception;

    /**
     * Saves a list of areaa to the datasource.
     *
     * @param areas a list of areas.
     * @return the saved areas.
     * @throws Exception if areas could not be saved.
     */
    List<Area> saveAreas(final Iterable<Area> areas) throws Exception;

    /**
     * Deletes an area from the datasource given an area id.
     *
     * @param id the id of the area.
     * @throws Exception if area could not be deleted.
     */
    void deleteArea(final Long id) throws Exception;

    /**
     * Deletes areas from the datasource given a list of areas.
     *
     * @param areas a list of areas.
     * @throws Exception if areas could not be deleted.
     */
    void deleteAreas(final Iterable<Area> areas) throws Exception;

}
