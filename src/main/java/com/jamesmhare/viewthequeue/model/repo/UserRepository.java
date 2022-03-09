package com.jamesmhare.viewthequeue.model.repo;

import com.jamesmhare.viewthequeue.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Enables a Repository for CRUD to the datasource for {@link User} objects.
 *
 * @author James Hare
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findAllByOrderByUserIdAsc();

    User findByEmail(final String email);

}