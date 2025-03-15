package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.models.UserEntityDetailsService;
import com.informationsecurity.PasteBinService.services.SecurityContextService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserEntityDetailsService userEntityDetailsService;

    @Autowired
    private SecurityContextService securityContextService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UserEntity());

        return "registration";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration_admin")
    public String regAdmin(Model model) {
        model.addAttribute("user", new UserEntity());

        return "reg_admin";
    }

    @PostMapping("/registration")
    public String registration(
            @ModelAttribute("user") UserEntity user,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (!userEntityDetailsService.saveUser(user, false)) {
            return "registration";
        }

        securityContextService.authorizeUser(user, request, response);

        return "redirect:/";
    }

    @PostMapping("/registration_admin")
    public String regAdmin(@ModelAttribute("user") UserEntity user) {
        if (!userEntityDetailsService.saveUser(user, true)) {
            return "reg_admin";
        }

        return "redirect:/";
    }

}
