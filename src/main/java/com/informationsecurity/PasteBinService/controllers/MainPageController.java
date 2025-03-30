package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.*;
import com.informationsecurity.PasteBinService.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class MainPageController {

    // Вот тут объявлять бины
    private final PasteService pasteService;
    private final SecurityContextService contextService;
    private final AdditionService additionService;
    private final UserEntityDetailsService userEntityDetailsService;
    private final TimeFormatService timeFormatService;
    private final TextFormatService textFormatService;

    // Вот тут объявлять не бины
    private int currentPage = 1;
    private Paste[] pastes;
    private int[] pagesArray;

    @Autowired
    public MainPageController(
            PasteService pasteService,
            SecurityContextService contextService,
            AdditionService additionService,
            UserEntityDetailsService userEntityDetailsService,
            TimeFormatService timeFormatService,
            TextFormatService textFormatService
    ) {
        this.pasteService = pasteService;
        this.contextService = contextService;
        this.additionService = additionService;
        this.userEntityDetailsService = userEntityDetailsService;
        this.timeFormatService = timeFormatService;
        this.textFormatService = textFormatService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {

        String userName = contextService.getUsername();

        List<Paste> pastes = pasteService.getAll();
        Collections.reverse(pastes);
        this.pastes = pastes.toArray(new Paste[0]);

        // Заполняем массив из номеров страниц
        this.pagesArray = new int[this.pastes.length / 10 + 1];
        for (int i = 0; i < pagesArray.length; i++) {
            pagesArray[i] = i + 1;
        }

//        // Вот эту хуйню я писал в пол первого ночи, поэтому
//        // просьба не хуесосить меня за этот кусок кода =).
//
//        // Если вкратце, этот код делит все посты по страницам,
//        // каждая страница содержит максимум 10 постов
//        /* //=================== |< Шедеврокод >| ===================// */
//        /**/ int pagesCount = pastes.size() / 10 + 1;                  /**/
//        /**/ int counter = 0;                                         /**/
//        /**/ for (int i = 1; i <= pagesCount; i++) {                  /**/
//        /**/     List<Paste> pagePastes = new ArrayList<>();          /**/
//        /**/     int iterator = Math.min(pastes.size() - counter, 10); /**/
//        /**/     for (int j = 0; j < iterator; j++) {                 /**/
//        /**/         pagePastes.add(pastes.get(counter));             /**/
//        /**/         counter++;                                       /**/
//        /**/     }                                                    /**/
//        /**/     pastesMap.put(i, pagePastes);                        /**/
//        /**/ }                                                        /**/
//        /* //=================== |< Шедеврокод >| ===================// */

        model
                .addAttribute("pasteList", getDividedPastes(1))
                .addAttribute("userName", userName)
                .addAttribute("authorities", contextService.getAuthorities())
                .addAttribute("pasteTime", timeFormatService)
                .addAttribute("textFormater", textFormatService)
                .addAttribute("pages", this.pagesArray);

        if (!userName.equals("anonymousUser")) {
            model.addAttribute(
                    "user",
                    userEntityDetailsService.loadUserByUsername(userName));
        }

        return "index";
    }

    @GetMapping("/page")
    public String nextPage(
            @RequestParam("q") String pageNum,
            Model model
    ) {
        if (isDigit(pageNum)) {
            currentPage = Integer.parseInt(pageNum);
        } else {
            if (pageNum.equals("next")) {
                currentPage =
                        (currentPage + 1) > this.pagesArray.length ?
                        currentPage :
                        currentPage + 1;
            } else if (pageNum.equals("prev")) {
                currentPage =
                        (currentPage - 1) < 1 ?
                        currentPage :
                        currentPage - 1;
            }
        }

        String userName = contextService.getUsername();

        model
                .addAttribute("pasteList", getDividedPastes(currentPage))
                .addAttribute("userName", userName)
                .addAttribute("authorities", contextService.getAuthorities())
                .addAttribute("pasteTime", timeFormatService)
                .addAttribute("textFormater", textFormatService)
                .addAttribute("pages", this.pagesArray)
                .addAttribute("userService", userEntityDetailsService);

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
        String userName = contextService.getUsername();


        model
                .addAttribute("paste", paste)
                .addAttribute("pasteTime", timeFormatService.getTimeFormat(paste.getExpirationTime()))
                .addAttribute("userName", userName)
                .addAttribute("authorities", contextService.getAuthorities())
                .addAttribute("addition", new Addition())
                .addAttribute("additions", additions)
                .addAttribute("timeFormater", timeFormatService)
                .addAttribute("index", 1);

        if (!userName.equals("anonymousUser")) {
            model.addAttribute(
                    "user",
                    userEntityDetailsService.loadUserByUsername(userName));
        }

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
        String userName = contextService.getUsername();

        model
                .addAttribute("userName", userName)
                .addAttribute("authorities", contextService.getAuthorities())
                .addAttribute("pasteTime", timeFormatService)
                .addAttribute("pasteList", pastes)
                .addAttribute("textFormater", textFormatService)
                .addAttribute("pages", this.pagesArray)
                .addAttribute("userService", userEntityDetailsService);

        if (!userName.equals("anonymousUser")) {
            model.addAttribute(
                    "user",
                    userEntityDetailsService.loadUserByUsername(userName));
        }

        return "index";
    }

    private boolean isDigit(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private Paste[] getDividedPastes(int page) {
        int index = 0;
        int start = page * 10 - 10;
        int stop = Math.min(page * 10, this.pastes.length);

        Paste[] pastes = new Paste[stop - start];

        for (int i = start; i < stop; i++) {
            pastes[index++] = this.pastes[i];
        }

        return pastes;
    }
}
