package com.jamesmhare.viewthequeue.model.repo;

import com.jamesmhare.viewthequeue.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAllByOrderByNameAsc();

    Role findByName(final String name);

}
