package com.jamesmhare.viewthequeue.service;

import com.jamesmhare.viewthequeue.model.attraction.Attraction;
import com.jamesmhare.viewthequeue.model.repo.AttractionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service class for communicating with the {@link AttractionRepository}.
 *
 * @author James Hare
 */
@Service
public class AttractionServiceImpl implements AttractionService {

    private final AttractionRepository repository;

    public AttractionServiceImpl(final AttractionRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attraction> findAllAttractions() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attraction> findAllAttractionsByThemeParkId(Long themeParkId) {
        return repository.findAllByThemeParkThemeParkId(themeParkId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attraction> findAllAttractionsByAreaId(Long areaId) {
        return repository.findAllByAreaAreaId(areaId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Attraction> findAttractionById(Long id) {
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attraction saveAttraction(Attraction attraction) throws Exception {
        return repository.save(attraction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attraction> saveAttractions(Iterable<Attraction> attractions) throws Exception {
        return repository.saveAll(attractions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAttraction(Long id) throws Exception {
        repository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAttractions(Iterable<Attraction> attractions) throws Exception {
        repository.deleteAll(attractions);
    }
}
