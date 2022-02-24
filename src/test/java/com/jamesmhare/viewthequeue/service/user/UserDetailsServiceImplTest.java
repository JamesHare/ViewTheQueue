package com.jamesmhare.viewthequeue.service.user;

import com.jamesmhare.viewthequeue.model.repo.UserRepository;
import com.jamesmhare.viewthequeue.model.user.Role;
import com.jamesmhare.viewthequeue.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Set;

public class UserDetailsServiceImplTest {

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
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsService = new UserDetailsServiceImpl(mockUserRepository);
    }

    @Test
    public void testLoadUserByUsername() {
        Mockito.when(mockUserRepository.findByEmail(email)).thenReturn(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Assertions.assertEquals(email, userDetails.getUsername());
        Assertions.assertEquals(password, userDetails.getPassword());
        Assertions.assertTrue(userDetails.isAccountNonExpired());
        Assertions.assertTrue(userDetails.isAccountNonLocked());
        Assertions.assertTrue(userDetails.isCredentialsNonExpired());
        Assertions.assertTrue(userDetails.isEnabled());
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        authorities.forEach(grantedAuthority -> Assertions.assertEquals("USER", grantedAuthority.getAuthority()));
    }

    @Test
    public void testLoadUserByUsername_UserIsNull() {
        Mockito.when(mockUserRepository.findByEmail(email)).thenReturn(null);
        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });

        String expectedMessage = "Could not find user with email " + email;
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

}
