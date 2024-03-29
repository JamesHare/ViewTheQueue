package com.jamesmhare.viewthequeue.service.user;

import com.jamesmhare.viewthequeue.model.user.Role;
import com.jamesmhare.viewthequeue.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

/**
 * A class containing Test Cases for the {@link UserDetailsImpl}.
 *
 * @author James Hare
 */
public class UserDetailsImplTest {

    private final String email = "test@domain.com";
    private final String firstName = "test";
    private final String password = "test_passwd";
    private final Role role = Role.builder().name("USER").build();
    private final User user = User.builder()
            .email(email)
            .firstName(firstName)
            .password(password)
            .enabled(true)
            .roles(Set.of(role))
            .build();
    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = new UserDetailsImpl(user);
    }

    @Test
    public void testGetAuthorities() {
        final Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        authorities.forEach(grantedAuthority -> Assertions.assertEquals("USER", grantedAuthority.getAuthority()));
    }

    @Test
    public void testGetPassword() {
        final String actualPassword = userDetails.getPassword();
        Assertions.assertEquals(password, actualPassword);
    }

    @Test
    public void testGetUsername() {
        final String actualUsername = userDetails.getUsername();
        Assertions.assertEquals(email, actualUsername);
    }

    @Test
    public void testIsAccountNonExpired() {
        final boolean nonExpired = userDetails.isAccountNonExpired();
        Assertions.assertTrue(nonExpired);
    }

    @Test
    public void testIsAccountNonLocked() {
        final boolean nonLocked = userDetails.isAccountNonLocked();
        Assertions.assertTrue(nonLocked);
    }

    @Test
    public void testIsCredentialsNonExpired() {
        final boolean credentialsNonExpired = userDetails.isCredentialsNonExpired();
        Assertions.assertTrue(credentialsNonExpired);
    }

    @Test
    public void testIsEnabled() {
        final boolean enabled = userDetails.isEnabled();
        Assertions.assertTrue(enabled);
    }

}
