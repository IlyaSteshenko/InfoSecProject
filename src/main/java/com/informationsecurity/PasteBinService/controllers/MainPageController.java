package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.Addition;
import com.informationsecurity.PasteBinService.models.Paste;
import com.informationsecurity.PasteBinService.services.AdditionService;
import com.informationsecurity.PasteBinService.services.PasteService;
import com.informationsecurity.PasteBinService.services.SecurityContextService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@AllArgsConstructor
public class MainPageController {

    private PasteService pasteService;
    private SecurityContextService contextService;
    private AdditionService additionService;
    private SecurityContextService securityContextService;

    @GetMapping("/")
    public String mainPage(Model model) {

        String userName = securityContextService.getUsername();

        model.addAttribute("pasteList", pasteService.getAll());
        model.addAttribute("userName", userName);
        model.addAttribute("authorities", contextService.getAuthorities());

        return "index";
    }

    @GetMapping("/paste/{id}")
    public String showFullPost(
            @PathVariable(name = "id") Long id,
            Model model
    ) {
        Paste paste = pasteService.findById(id);
        List<Addition> additions = additionService.findAdditionsWithPasteId(id);

        model.addAttribute("paste", paste);
        model.addAttribute("pasteTime", DateTimeFormatter.ofPattern("hh:mm:ss dd-MM-yyyy").format(paste.getExpirationTime()));
        model.addAttribute("userName", contextService.getUsername());
        model.addAttribute("addition", new Addition());
        model.addAttribute("additions", additions);
        model.addAttribute("addTime", DateTimeFormatter.ofPattern("hh:mm:ss dd-MM-yyyy"));
        model.addAttribute("index", 1);

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
