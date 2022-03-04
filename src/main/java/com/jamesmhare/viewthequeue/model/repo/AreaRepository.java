package com.jamesmhare.viewthequeue.model.repo;

import com.jamesmhare.viewthequeue.model.area.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    List<Area> findAllByThemeParkThemeParkId(final Long themeParkId);

}
