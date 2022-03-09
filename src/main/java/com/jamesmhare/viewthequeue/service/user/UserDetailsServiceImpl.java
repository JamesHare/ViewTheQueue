package com.jamesmhare.viewthequeue.service.user;

import com.jamesmhare.viewthequeue.model.repo.UserRepository;
import com.jamesmhare.viewthequeue.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serves as an implementation of the {@link UserDetailsService}.
 *
 * @author James Hare
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user associated with a given email address.
     *
     * @param email an email address.
     * @return the user associates with a given email address.
     * @throws UsernameNotFoundException if the given email address does not match any User.
     */
    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {
        final User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user with email " + email);
        }

        return new UserDetailsImpl(user);
    }

}