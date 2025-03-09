package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.Addition;
import com.informationsecurity.PasteBinService.models.Paste;
import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.models.UserEntityDetailsService;
import com.informationsecurity.PasteBinService.services.AdditionService;
import com.informationsecurity.PasteBinService.services.PasteService;
import com.informationsecurity.PasteBinService.services.SecurityContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class MainPageController {

    @Autowired
    private PasteService pasteService;

    @Autowired
    private UserEntityDetailsService userEntityDetailsService;

    @Autowired
    private SecurityContextService contextService;

    @Autowired
    private AdditionService additionService;

    @GetMapping("/")
    public String mainPage(Model model) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("pasteList", pasteService.getAll());
        model.addAttribute("userName", userName);
        model.addAttribute("authorities", contextService.getAuthorities());

//        Addition addition = new Addition();
//        addition.setPasteId(1);
//        addition.setAdditionDate(LocalDateTime.now());
//        addition.setUpdateName("Name");
//        addition.setUpdateText("Text");
//        additionService.save(addition);

        return "index";
    }

    @GetMapping("/paste/{id}")
    public String showFullPost(
            @PathVariable(name = "id") Long id,
            Model model
    ) {
        Paste paste = pasteService.findById(id);
        model.addAttribute("paste", paste);
        model.addAttribute("userName", contextService.getUsername());
        model.addAttribute("addition", new Addition());
        model.addAttribute("additions", additionService.findAdditionsWithPasteId(id));

        return "full_paste";
    }

    @PostMapping("/paste/{id}")
    public String addUpdate(
            @ModelAttribute("addition") Addition addition,
            @PathVariable("id") int id
    ) {

        Addition newAddition = new Addition();
        newAddition.setPasteId(id);
        newAddition.setAdditionDate(LocalDateTime.now());
        newAddition.setUpdateName(addition.getUpdateName());
        newAddition.setUpdateText(addition.getUpdateText());

        additionService.save(newAddition);

        return "redirect:/paste/" + id;
    }

}
