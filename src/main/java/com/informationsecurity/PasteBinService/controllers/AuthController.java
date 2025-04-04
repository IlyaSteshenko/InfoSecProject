package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.services.UserEntityDetailsService;
import com.informationsecurity.PasteBinService.services.SecurityContextService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
        model
                .addAttribute("user", new UserEntity())
                .addAttribute("error", "");

        return "registration";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserEntity());
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
            HttpServletResponse response,
            Model model
    ) {

//        String pattern = "[^!+=;&%$#@*()~{},. ]";
//        if (!(
//                user.getPassword().matches(pattern) &&
//                user.getPassword().length() >= 7
//        )) {
//            model.addAttribute(
//                    "error",
//                    "Вы ввели некорректный пароль. Пароль должен содержать не менее 7 символов и не содержать символы [!,+,=,;,&,%,$,#,@,*,(,),~,{,}], точки, запятые и пробелы"
//            );
//            return "registration";
//        }

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
