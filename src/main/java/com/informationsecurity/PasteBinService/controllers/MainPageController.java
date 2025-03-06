package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.Paste;
import com.informationsecurity.PasteBinService.services.PasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {

    @Autowired
    private PasteService pasteService;

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("pasteList", pasteService.getAll());
        model.addAttribute("searchRequest", "");

        return "index";
    }

    @GetMapping("/paste/{id}")
    public String showFullPost(
            @PathVariable(name = "id") Long id,
            Model model
    ) {
        Paste paste = pasteService.findById(id);
        model.addAttribute("paste", paste);

        return "full_paste";
    }

    @GetMapping("/search")
    public void search(@ModelAttribute(name = "searchRequest") String searchRequest) {
//        List<Paste> pasteList = pasteService.getAll();

        System.out.println(searchRequest);
    }

}
