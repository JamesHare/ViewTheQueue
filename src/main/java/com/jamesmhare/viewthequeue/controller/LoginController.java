package com.jamesmhare.viewthequeue.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The Controller responsible for handling User Login functionality.
 *
 * @author James Hare
 */
@Slf4j
@Controller
public class LoginController {

    public LoginController() {
    }

    /**
     * Returns the User Login View.
     *
     * @return the User Login View.
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }

}
