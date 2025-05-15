package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.Code;
import com.informationsecurity.PasteBinService.models.Email;
import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class RecoverPasswordController {

    private final EmailSenderService emailSenderService;
    private final UniqueCodeGeneratorService uniqueCodeGeneratorService;
    private final CodeStorage codeStorage;
    private final UserEntityDetailsService userEntityDetailsService;
    private final SecurityContextService contextService;

    private String email;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    private Code tempCode;
    private boolean isCodeChecked = false;

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

    /// На этот эндпоинт делай post-запрос
    /// с почтой в виде json
    /// с ключом email
    @PostMapping("/recover_password")
    public ResponseEntity<String> recoverPassword(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        String mail = jsonObject.getString("email");

        if (userEntityDetailsService.findUserByEmail(mail) == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Email not found");
        }

        contextService.unauthorizeUser();

        Code tempCode = uniqueCodeGeneratorService.generateCode();

        String text = tempCode.getCode() +
                " - ваш код для восстановления пароля";

        Email email = new Email();
        email.setRecipient(mail);
        email.setSubject("Восстановление пароля");
        email.setTitle("Код подтверждения");
        email.setMessage(text);

        codeStorage.add(email.getRecipient(), tempCode);
        emailSenderService.sendVerificationEmail(email);

        this.email = email.getRecipient();

        return ResponseEntity.ok().build();
    }

    /// На этот эндпоинт делай post-запрос
    /// с кодом в виде json
    /// с ключом code
    @PostMapping("/send_code")
    public ResponseEntity<String> checkCode(
            @RequestBody String json,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        JSONObject jsonObject = new JSONObject(json);
        Code code = new Code(
                jsonObject.getString("code"),
                LocalDateTime.now().plusSeconds(90)
        );

        if (codeStorage.checkCode(this.email, code)) {
            contextService.authorizeUser(
                    userEntityDetailsService.findUserByEmail(email),
                    request, response
            );

            isCodeChecked = true;

            codeStorage.removeCode(this.email);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    /// На этот эндпоинт делай post-запрос
    /// с кодом в виде json
    /// с ключом password
    @PostMapping("/new_password")
    public ResponseEntity<String> newPassword(@RequestBody String json) {

        if (!isCodeChecked) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .build();
        }

        JSONObject jsonObject = new JSONObject(json);

        String password = passwordEncoder.encode(
                jsonObject.getString("password")
        );

        UserEntity userWithNewPassword = userEntityDetailsService.findUserByEmail(email);
        userWithNewPassword.setPassword(password);
        userEntityDetailsService.saveAsUser(userWithNewPassword);
        isCodeChecked = false;
        return ResponseEntity.ok().build();
    }
}
