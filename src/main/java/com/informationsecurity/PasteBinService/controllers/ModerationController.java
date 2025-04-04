package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.Email;
import com.informationsecurity.PasteBinService.services.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/write_to_moderator")
public class ModerationController {

    private final EmailSenderService sender;

    @Autowired
    public ModerationController(EmailSenderService sender) {
        this.sender = sender;
    }

    @GetMapping
    public String writeToModerator(Model model) {
        model.addAttribute("message", new Email());
        return "moderation";
    }

    @PostMapping
    public String writeToModerator(@ModelAttribute("message") Email message) {
        Email email = new Email();

        StringBuilder mes = new StringBuilder();
        mes
                .append("Обращение от ")
                .append(message.getRecipient())
                .append(": \n\n")
                .append(message.getMessage());

        email.setMessage(mes.toString());
        email.setSubject("Обращение к модератору");
        email.setTitle("Обращение к модератору");

        sender.sendEmailToModerator(email);

        return "redirect:/";
    }

}
