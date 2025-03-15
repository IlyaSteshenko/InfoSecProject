package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.Paste;
import com.informationsecurity.PasteBinService.services.PasteService;
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

    @GetMapping
    public String createNewPaste(Model model) {

        model.addAttribute("paste", new Paste());
        model.addAttribute("userName", getUsername());

        return "create_new_paste";
    }

    @PostMapping
    public String savePaste(@ModelAttribute("paste") Paste paste) {
        if (paste == null) {
            return "create_new_paste";
        }
        paste.setExpirationTime(LocalDateTime.now());
        paste.setAuthor(getUsername());
        pasteService.save(paste);

        return "redirect:/";
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
