package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.Code;
import com.informationsecurity.PasteBinService.models.Email;
import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
public class RecoverPasswordController {

    private final EmailSenderService emailSenderService;
    private final UniqueCodeGeneratorService uniqueCodeGeneratorService;
    private final CodeStorage codeStorage;
    private final UserEntityDetailsService userEntityDetailsService;
    private final SecurityContextService contextService;

    private String email;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private Code tempCode;

    @Autowired
    public RecoverPasswordController(
            EmailSenderService emailSenderService,
            UniqueCodeGeneratorService uniqueCodeGeneratorService,
            CodeStorage codeStorage,
            UserEntityDetailsService userEntityDetailsService,
            SecurityContextService contextService
    ) {
        this.emailSenderService = emailSenderService;
        this.uniqueCodeGeneratorService = uniqueCodeGeneratorService;
        this.codeStorage = codeStorage;
        this.userEntityDetailsService = userEntityDetailsService;
        this.contextService = contextService;
    }

    @GetMapping("/recover_password")
    public String recoverPassword(Model model) {

        model
                .addAttribute("email", new Email())
                .addAttribute("error", "");

        return "recover_password";
    }

    @PostMapping("/recover_password")
    public String recoverPassword(
            @ModelAttribute("email") Email email,
            Model model
    ) {

        if (userEntityDetailsService.findUserByEmail(email.getRecipient()) == null) {
            model.addAttribute("error", "Пользователя с такой почтой не существует =(");
            return "recover_password";
        }

        this.tempCode = uniqueCodeGeneratorService.generateCode();
        StringBuilder text = new StringBuilder();

        text
                .append(this.tempCode.getCode())
                .append(" - ваш код для восстановления пароля");

        email.setSubject("Восстановление пароля");
        email.setTitle("Код подтверждения");
        email.setMessage(text.toString());

        codeStorage.add(email.getRecipient(), this.tempCode);
        emailSenderService.send(email);

        this.email = email.getRecipient();

        return "redirect:/send_code";
    }

    @GetMapping("/send_code")
    public String checkCode(Model model) {
        model
                .addAttribute("mainCode", new Code())
                .addAttribute("error", "");
        return "send_code";
    }

    @PostMapping("/send_code")
    public String checkCode(
            @ModelAttribute("mainCode") Code code,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        code.setExpirationTime(this.tempCode.getExpirationTime());
        System.out.println(code.getCode());
        System.out.println(code.getExpirationTime());
        if (codeStorage.checkCode(this.email, code)) {
//            UserEntity user = userEntityDetailsService.findUserByEmail(email);
            contextService.authorizeUser(
                    userEntityDetailsService.findUserByEmail(email),
                    request, response
            );

            codeStorage.removeCode(this.email);
            return "redirect:/new_password";
        } else {
            model.addAttribute("error", "Ваш код доступа истёк.\nПолучите новый код по этой ссылке: ");
            return "send_code";
        }
    }

    @GetMapping("/new_password")
    public String newPassword(Model model) {
        model
                .addAttribute("user", new UserEntity())
                .addAttribute(
                        "userName",
                        userEntityDetailsService.findUserByEmail(email).getUsername()
                );

        return "new_password";
    }

    @PostMapping("/new_password")
    public String newPassword(@ModelAttribute("user") UserEntity user) {
        UserEntity userWithNewPassword = userEntityDetailsService.findUserByEmail(email);
        userWithNewPassword.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntityDetailsService.saveAsUser(userWithNewPassword);
        return "redirect:/login";
    }
}
