package com.jamesmhare.viewthequeue.service.user;

import com.jamesmhare.viewthequeue.model.user.Role;
import com.jamesmhare.viewthequeue.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Provides functionality around a User that Spring will use for endpoint AuthZ and Login.
 *
 * @author James Hare
 */
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(final User user) {
        this.user = user;
    }

    /**
     * Gets a Collection of {@link GrantedAuthority} objects for a User.
     *
     * @return a Collection of Granted Authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<Role> roles = user.getRoles();
        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (final Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    /**
     * Gets the User's password.
     *
     * @return the User's password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Gets the User's username.
     *
     * @return the User's username.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Returns whether the user account is expired or not.
     *
     * @return true if the user account is not expired, false if the user account is expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Returns whether the user account is locked or not.
     *
     * @return true if the user account is not locked, false if the user account is locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Returns whether the user account credentials is expired or not.
     *
     * @return true if the user account credentials is not expired, false if the user account credentials is expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns whether the user account is enabled or not.
     *
     * @return true if the user account is enabled, false if the user account is not enabled.
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
