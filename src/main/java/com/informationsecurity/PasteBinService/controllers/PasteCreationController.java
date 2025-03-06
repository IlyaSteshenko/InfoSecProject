package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.Paste;
import com.informationsecurity.PasteBinService.models.PasteAddition;
import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.services.PasteService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PasteCreationController {

    @Autowired
    private PasteService pasteService;

    @GetMapping
    public String createNewPaste(Model model) {
        model.addAttribute("paste", new Paste());
        model.addAttribute("addition", new PasteAddition());

        return "create_new_paste";
    }

    @PostMapping
    public String savePaste(@ModelAttribute("paste") Paste paste) {
        if (paste == null) {
            return "create_new_paste";
        }
        paste.setExpirationTime(LocalDateTime.now());
        pasteService.save(paste);

        return "redirect:/";
    }

    @PostMapping("/save_changes")
    public String saveChanges(
            @ModelAttribute("addition") PasteAddition pasteAddition,
            @ModelAttribute("paste") Paste paste
    ) {
        pasteAddition.setPasteId(paste.getId());

        return "redirect:/create_new_paste";
    }
}
