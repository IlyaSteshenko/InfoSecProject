package com.informationsecurity.PasteBinService.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("")
    private String showAdminPanel() {
        return "<h2>Admin Panel</h2>";
    }

}
