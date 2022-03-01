package com.jamesmhare.viewthequeue.model.repo;

import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeParkRepository extends JpaRepository<ThemePark, Long> {
}
