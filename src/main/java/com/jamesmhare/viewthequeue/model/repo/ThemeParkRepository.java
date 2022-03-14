package com.jamesmhare.viewthequeue.model.repo;

import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Enables a Repository for CRUD to the datasource for {@link ThemePark} objects.
 *
 * @author James Hare
 */
@Repository
public interface ThemeParkRepository extends JpaRepository<ThemePark, Long> {
}
