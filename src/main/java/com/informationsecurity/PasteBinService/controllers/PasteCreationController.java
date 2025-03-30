package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.Paste;
import com.informationsecurity.PasteBinService.services.UserEntityDetailsService;
import com.informationsecurity.PasteBinService.services.PasteService;
import com.informationsecurity.PasteBinService.services.SecurityContextService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/create_new_paste")
@AllArgsConstructor
public class PasteCreationController {

    private final PasteService pasteService;
    private final SecurityContextService securityContextService;
    private final UserEntityDetailsService userEntityDetailsService;

    @GetMapping
    public String createNewPaste(Model model) {

        String userName = securityContextService.getUsername();

        model
                .addAttribute("paste", new Paste())
                .addAttribute("userName", getUsername());

        if (!userName.equals("anonymousUser")) {
            model.addAttribute(
                    "user",
                    userEntityDetailsService.loadUserByUsername(userName));
        }

        return "create_new_paste";
    }

    @PostMapping
    public String savePaste(@ModelAttribute("paste") Paste paste) {
        if (paste == null) {
            return "create_new_paste";
        }
        paste.setExpirationTime(LocalDateTime.now());
        paste.setAuthor(getUsername());
        paste.setAuthorId(userEntityDetailsService.findByName(getUsername()).getId());
        pasteService.save(paste);

        return "redirect:/";
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
