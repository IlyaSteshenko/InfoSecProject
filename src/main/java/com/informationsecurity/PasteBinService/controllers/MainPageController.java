package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.*;
import com.informationsecurity.PasteBinService.services.AdditionService;
import com.informationsecurity.PasteBinService.services.PasteService;
import com.informationsecurity.PasteBinService.services.SecurityContextService;
import com.informationsecurity.PasteBinService.services.TimeFormatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class MainPageController {

    private final PasteService pasteService;
    private final SecurityContextService contextService;
    private final AdditionService additionService;
    private final SecurityContextService securityContextService;
    private final UserEntityDetailsService userEntityDetailsService;
    private final TimeFormatService timeFormatService;
    private final String timeFormat = "hh:mm:ss dd MM yyyy";

    @GetMapping("/")
    public String mainPage(Model model) {

        String userName = securityContextService.getUsername();

        model.addAttribute("pasteList", pasteService.getAll());
        model.addAttribute("userName", userName);
        model.addAttribute("authorities", contextService.getAuthorities());
        model.addAttribute("pasteTime", timeFormatService);

        if (!userName.equals("anonymousUser")) {
            model.addAttribute(
                    "user",
                    userEntityDetailsService.loadUserByUsername(userName));
        }

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
        model.addAttribute("pasteTime", timeFormatService.getTimeFormat(paste.getExpirationTime()));
        model.addAttribute("userName", contextService.getUsername());
        model.addAttribute("addition", new Addition());
        model.addAttribute("additions", additions);
        model.addAttribute("addTime", timeFormatService);
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

    @PostMapping("/search")
    public String searchPatents(
            Model model,
            @RequestBody String search
    ) {
        String searchString = URLDecoder.decode(search.substring(7), StandardCharsets.UTF_8);
        List<Paste> pastes = pasteService.findPatentsWithText(searchString);
        String userName = securityContextService.getUsername();

        model.addAttribute("userName", userName);
        model.addAttribute("authorities", contextService.getAuthorities());
        model.addAttribute("pasteTime", timeFormatService);
        model.addAttribute("pasteList", pastes);
        return "index";
    }
}
