package com.jamesmhare.viewthequeue.model.repo;

import com.jamesmhare.viewthequeue.model.attraction.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Enables a Repository for CRUD to the datasource for {@link Attraction} objects.
 *
 * @author James Hare
 */
@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {

    List<Attraction> findAllByAreaAreaId(final Long areaId);

    List<Attraction> findAllByThemeParkThemeParkId(final Long themeParkId);

}
