package com.jamesmhare.viewthequeue.model.repo;

import com.jamesmhare.viewthequeue.model.user.PasswordResetToken;
import com.jamesmhare.viewthequeue.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    PasswordResetToken findByUser(final User user);

    Optional<PasswordResetToken> findPasswordResetTokenByUserEmail(final String email);

    List<PasswordResetToken> findAllByExpiryDateBefore(final Date date);

}
