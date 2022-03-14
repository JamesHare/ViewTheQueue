package com.jamesmhare.viewthequeue.model.repo;

import com.jamesmhare.viewthequeue.model.user.User;
import com.jamesmhare.viewthequeue.model.user.UserAccountVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Enables a Repository for CRUD to the datasource for {@link UserAccountVerification} objects.
 *
 * @author James Hare
 */
public interface UserAccountVerificationRepository extends JpaRepository<UserAccountVerification, UUID> {

    UserAccountVerification findByUser(final User user);

    Optional<UserAccountVerification> findUserAccountVerificationByUserEmail(final String email);

    List<UserAccountVerification> findAllByExpiryDateBefore(final Date date);

}
