package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.UserEntityDetailsService;
import com.informationsecurity.PasteBinService.services.PasteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
@AllArgsConstructor
public class UserProfileController {

    private final UserEntityDetailsService userEntityDetailsService;
    private final PasteService pasteService;

    @GetMapping("/{id}")
    public String mainProfilePage(
            Model model,
            @PathVariable("id") Long id
    ) {
        model
                .addAttribute("user", userEntityDetailsService.findById(id))
                .addAttribute("text", pasteService.getAll().get(8).getText());
        return "profile_page";
    }

}
