package com.jamesmhare.viewthequeue.service;

import com.jamesmhare.viewthequeue.model.themepark.ThemePark;

import java.util.List;
import java.util.Optional;

/**
 * In interface describing the Theme Park Service.
 *
 * @author James Hare
 */
public interface ThemeParkService {

    /**
     * Finds and returns all the theme parks in the datasource.
     *
     * @return a list of theme parks.
     */
    List<ThemePark> findAllThemeParks();

    /**
     * Finds and returns a theme park matching a given id.
     *
     * @param id a theme park id.
     * @return an Optional containing either the theme park or empty.
     */
    Optional<ThemePark> findThemeParkById(final Long id);

    /**
     * Saves a theme park to the datasource.
     *
     * @param themePark park a theme park.
     * @return the saved theme park.
     */
    ThemePark saveThemePark(final ThemePark themePark) throws Exception;

    /**
     * Deletes a theme park from the datasource given a theme park id.
     *
     * @param id the id of the theme park.
     */
    void deleteThemePark(final Long id) throws Exception;

}
