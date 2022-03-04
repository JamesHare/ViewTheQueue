package com.jamesmhare.viewthequeue.service;

import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.model.repo.AreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service class for communicating with the {@link AreaRepository}.
 *
 * @author James Hare
 */
@Service
public class AreaServiceImpl implements AreaService {

    private final AreaRepository repository;

    public AreaServiceImpl(final AreaRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Area> findAllAreas() {
        return this.repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Area> findAllAreasByThemeParkId(final Long themeParkId) {
        return this.repository.findAllByThemeParkThemeParkId(themeParkId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Area> findAreaById(final Long id) {
        return this.repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Area saveArea(final Area area) throws Exception {
        return repository.save(area);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Area> saveAreas(final Iterable<Area> areas) throws Exception {
        return repository.saveAll(areas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteArea(final Long id) throws Exception {
        repository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAreas(final Iterable<Area> areas) throws Exception {
        repository.deleteAll(areas);
    }

}
