package com.jamesmhare.viewthequeue.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A class containing Test Cases for the {@link LoginController}.
 *
 * @author James Hare
 */
public class LoginControllerTest {

    private LoginController controller;

    @BeforeEach
    public void setUp() {
        controller = new LoginController();
    }

    @Test
    public void testLogin() {
        final String login = controller.login();
        Assertions.assertEquals("login", login);
    }

}
