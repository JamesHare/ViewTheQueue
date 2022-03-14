package com.jamesmhare.viewthequeue.config;

import com.jamesmhare.viewthequeue.model.repo.UserRepository;
import com.jamesmhare.viewthequeue.service.user.UserDetailsServiceImpl;
import lombok.Generated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Handles the security config beans necessary for Role Based AuthN.
 *
 * @author James Hare
 */
@Generated
@Configuration
@EnableWebSecurity
@EnableScheduling
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final UserRepository userRepository;

    public WebSecurityConfig(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new UserDetailsService Bean.
     *
     * @return the User Details Service Bean.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }

    /**
     * Creates a new BCryptPasswordEncoder Bean.
     *
     * @return the BCrypt Password Encoder Bean.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a new DaoAuthenticationProvider Bean which pulls together the user details service
     * and the password encoder.
     *
     * @return the DaoAuthenticationProvider Bean.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures the web security config with the authentication provider.
     *
     * @param auth the Authentication Manager Builder.
     */
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    /**
     * Configures Http Security across the web application. Defines which roles are required to
     * access specific endpoints, along with the login and logout endpints.
     *
     * @param http an HttpSecurity object.
     * @throws Exception if an error occurred when updating the HttpSecurity object.
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/associate/**").hasAnyAuthority("ASSOCIATE")
                .antMatchers("/dashboard/**").hasAnyAuthority("ADMIN", "ASSOCIATE", "USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/").failureUrl("/login?error=true").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling().accessDeniedPage("/403");
    }
}
