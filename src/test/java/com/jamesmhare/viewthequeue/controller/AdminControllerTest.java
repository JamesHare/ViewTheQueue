package com.jamesmhare.viewthequeue.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

public class AdminControllerTest {

    private AdminController controller;

    @Mock
    private Model mockModel;

    @BeforeEach
    public void setUp() {
        controller = new AdminController();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowAdminDashboard() {
        String actual = controller.showAdminDashboard(mockModel);
        Assertions.assertEquals("/admin/admin-dashboard", actual);
    }

}
