package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.*;
import com.informationsecurity.PasteBinService.services.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
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
//     private final UserEntityRepository userEntityRepository;
    private final TextFormatService textFormatService;

//     private final String timeFormat = "hh:mm:ss dd MM yyyy";

    @GetMapping("/")
    public String mainPage(Model model) {

        String userName = securityContextService.getUsername();
        List<Paste> pastes = pasteService.getAll();
        Collections.reverse(pastes);

        model
                .addAttribute("pasteList", pastes)
                .addAttribute("userName", userName)
                .addAttribute("authorities", contextService.getAuthorities())
                .addAttribute("pasteTime", timeFormatService)
                .addAttribute("textFormater", textFormatService);

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

        model
                .addAttribute("paste", paste)
                .addAttribute("pasteTime", timeFormatService.getTimeFormat(paste.getExpirationTime()))
                .addAttribute("userName", contextService.getUsername())
                .addAttribute("addition", new Addition())
                .addAttribute("additions", additions)
                .addAttribute("addTime", timeFormatService)
                .addAttribute("index", 1);

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

    @GetMapping("/search")
    public String searchPatents(
            @RequestParam("query") String search,
            Model model
    ) {
        String searchString = URLDecoder.decode(search, StandardCharsets.UTF_8);
        List<Paste> pastes = pasteService.findPatentsWithText(searchString);
        String userName = securityContextService.getUsername();

        model.addAttribute("userName", userName);
        model.addAttribute("authorities", contextService.getAuthorities());
        model.addAttribute("pasteTime", timeFormatService);
        model.addAttribute("pasteList", pastes)
                .addAttribute("textFormater", textFormatService);

        if (!userName.equals("anonymousUser")) {
            model.addAttribute(
                    "user",
                    userEntityDetailsService.loadUserByUsername(userName));
        }

        return "index";
    }
}
