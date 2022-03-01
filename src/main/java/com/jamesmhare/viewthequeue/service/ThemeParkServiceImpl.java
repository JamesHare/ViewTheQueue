package com.jamesmhare.viewthequeue.service;

import com.jamesmhare.viewthequeue.model.repo.ThemeParkRepository;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service class for communicating with the {@link ThemeParkRepository}.
 *
 * @author James Hare
 */
@Service
public class ThemeParkServiceImpl implements ThemeParkService {

    private final ThemeParkRepository repository;

    public ThemeParkServiceImpl(final ThemeParkRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    public List<ThemePark> findAllThemeParks() {
        return this.repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    public Optional<ThemePark> findThemeParkById(final Long id) {
        return this.repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    public ThemePark saveThemePark(final ThemePark themePark) throws Exception {
        return this.repository.save(themePark);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteThemePark(final Long id) throws Exception {
        this.repository.deleteById(id);
    }

}
