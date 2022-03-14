package com.jamesmhare.viewthequeue.service;


import com.jamesmhare.viewthequeue.model.attraction.Attraction;

import java.util.List;
import java.util.Optional;

/**
 * In interface describing the Attraction Service.
 *
 * @author James Hare
 */
public interface AttractionService {

    /**
     * Finds and returns all the attractions in the datasource.
     *
     * @return a list of attractions.
     */
    List<Attraction> findAllAttractions();

    /**
     * Finds and returns all the attractions of a theme park in the datasource.
     *
     * @param themeParkId a theme park id.
     * @return a list of attractions in the theme park.
     */
    List<Attraction> findAllAttractionsByThemeParkId(final Long themeParkId);

    /**
     * Finds and returns all the attractions of an area in the datasource.
     *
     * @param areaId an area id.
     * @return a list of attractions in the area.
     */
    List<Attraction> findAllAttractionsByAreaId(final Long areaId);

    /**
     * Finds and returns an attraction matching a given id.
     *
     * @param id an attraction id.
     * @return an Optional containing either the attraction or empty.
     */
    Optional<Attraction> findAttractionById(final Long id);

    /**
     * Saves an attraction to the datasource.
     *
     * @param attraction an attraction.
     * @return the saved attraction.
     * @throws Exception if attraction could not be saved.
     */
    Attraction saveAttraction(final Attraction attraction) throws Exception;

    /**
     * Saves a list of attractions to the datasource.
     *
     * @param attractions a list of attractions.
     * @return the saved attractions.
     * @throws Exception if attractions could not be saved.
     */
    List<Attraction> saveAttractions(final Iterable<Attraction> attractions) throws Exception;

    /**
     * Deletes an attraction from the datasource given an attraction id.
     *
     * @param id the id of the attraction.
     * @throws Exception if attraction could not be deleted.
     */
    void deleteAttraction(final Long id) throws Exception;

    /**
     * Deletes attractions from the datasource given a list of attractions.
     *
     * @param attractions a list of attractions.
     * @throws Exception if attractions could not be deleted.
     */
    void deleteAttractions(final Iterable<Attraction> attractions) throws Exception;

}
