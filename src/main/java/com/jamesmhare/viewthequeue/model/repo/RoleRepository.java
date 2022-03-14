package com.jamesmhare.viewthequeue.model.repo;

import com.jamesmhare.viewthequeue.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Enables a Repository for CRUD to the datasource for {@link Role} objects.
 *
 * @author James Hare
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAllByOrderByNameAsc();

    Role findByName(final String name);

}
