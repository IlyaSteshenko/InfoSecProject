package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.services.UserEntityDetailsService;
import com.informationsecurity.PasteBinService.services.PasteService;
import com.informationsecurity.PasteBinService.services.SecurityContextService;
import com.informationsecurity.PasteBinService.services.TextFormatService;
import com.informationsecurity.PasteBinService.services.TimeFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    // Вот тут объявлять бины
    private final UserEntityDetailsService userEntityDetailsService;
    private final PasteService pasteService;
    private final SecurityContextService contextService;
    private final TimeFormatService timeFormatService;
    private final TextFormatService textFormatService;

    // Вот тут объявлять не бины
    private Long userId;

    @Autowired
    public UserProfileController(
            UserEntityDetailsService userEntityDetailsService,
            PasteService pasteService,
            SecurityContextService contextService,
            TimeFormatService timeFormatService,
            TextFormatService textFormatService
    ) {
        this.userEntityDetailsService = userEntityDetailsService;
        this.pasteService = pasteService;
        this.contextService = contextService;
        this.timeFormatService = timeFormatService;
        this.textFormatService = textFormatService;
    }

    @GetMapping("/{id}")
    public String mainProfilePage(
            Model model,
            @PathVariable("id") Long id
    ) {
        UserEntity user = userEntityDetailsService.findById(id);
        String userName = contextService.getUsername();
        userId = id;

        model
                .addAttribute("userMain", user)
                .addAttribute("userName", userName)
                .addAttribute("patents", pasteService.findPatentsByAuthor(user.getUsername()))
                .addAttribute("authorities", contextService.getAuthorities())
                .addAttribute("pasteTime", timeFormatService)
                .addAttribute("textFormater", textFormatService);

        if (!userName.equals("anonymousUser")) {
            model.addAttribute(
                    "user",
                    userEntityDetailsService.loadUserByUsername(userName));
        }

        return "profile";
    }

    @PostMapping("/save_description")
    public String saveDescription(@ModelAttribute("userMain") UserEntity user) {
        UserEntity newUser = userEntityDetailsService.findById(userId);
        newUser.setDescription(user.getDescription());
        userEntityDetailsService.saveAsUser(newUser);
        return "redirect:/profile/" + newUser.getId();
    }

}
