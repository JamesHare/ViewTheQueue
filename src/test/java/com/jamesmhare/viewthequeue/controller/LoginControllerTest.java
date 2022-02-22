package com.jamesmhare.viewthequeue.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginControllerTest {

    private LoginController controller;

    @BeforeEach
    public void setUp() {
        controller = new LoginController();
    }

    @Test
    public void testLogin() {
        String login = controller.login();
        Assertions.assertEquals("login", login);
    }

}
