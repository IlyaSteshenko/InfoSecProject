package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.models.UserEntityDetailsService;
import com.informationsecurity.PasteBinService.services.SecurityContextService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserEntityDetailsService userEntityDetailsService;
    private final SecurityContextService securityContextService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UserEntity());

        return "registration";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserEntity());
        return "login";
    }

//    @PostMapping("/login")
//    public String login(
//            @ModelAttribute("user") UserEntity user,
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) {
//
//        if (response.getStatus() == 403) {
//            return "redirect:/login";
//        }
//
//        UserEntity authorizedUser = (UserEntity) userEntityDetailsService.loadUserByUsername(user.getUsername());
//
//        SignInRequest signInRequest = new SignInRequest();
//        signInRequest.setUsername(user.getUsername());
//        signInRequest.setPassword(user.getPassword());
//
//        securityContextService.authorizeUser(authorizedUser, request, response);
//
//        return "redirect:/";
//    }

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

//        SignUpRequest signUpRequest = new SignUpRequest();
//        signUpRequest.setUsername(user.getUsername());
//        signUpRequest.setPassword(user.getPassword());
//        signUpRequest.setEmail(user.getEmail());
//
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

    @GetMapping("/recover_password")
    public String recoverPassword() {
        return "recover_password";
    }

}
