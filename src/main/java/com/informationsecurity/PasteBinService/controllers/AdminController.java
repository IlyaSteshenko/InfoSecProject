package com.informationsecurity.PasteBinService.controllers;

import com.informationsecurity.PasteBinService.models.Role;
import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.models.UserEntityDetailsService;
import com.informationsecurity.PasteBinService.services.SecurityContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserEntityDetailsService userEntityDetailsService;

    @Autowired
    private SecurityContextService contextService;

    @GetMapping
    private String showAdminPanel(Model model) {
        List<UserEntity> usersList = userEntityDetailsService.allUsers();
        model.addAttribute("users", usersList);
        model.addAttribute("adminName", contextService.getUsername());
        return "admin_panel";
    }

    @PostMapping
    public String saveChanges(@ModelAttribute(name = "users") List<UserEntity> users) {
        for (UserEntity user : users) {
            userEntityDetailsService.saveAsUser(user);
        }

        return "redirect:/admin";
    }

    // Удаление пользователя
    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        if (userEntityDetailsService.deleteById(id)) {
            return "redirect:/admin";
        }
        return "redirect:/admin";
    }

    // Блокирование пользователя
    @PostMapping("/user/{id}/ban")
    public String banUser(@PathVariable(name = "id") Long id) {
        UserEntity user = userEntityDetailsService.findById(id);
        user.setAccountLocked(true);
        userEntityDetailsService.saveAsUser(user);

        return "redirect:/admin";
    }

    @PostMapping("/user/{id}/unban")
    public String unbanUser(@PathVariable(name = "id") Long id) {
        UserEntity user = userEntityDetailsService.findById(id);
        user.setAccountLocked(false);
        userEntityDetailsService.saveAsUser(user);

        return "redirect:/admin";
    }

    @PostMapping("/user/{id}/change_to_user")
    public String changeRoleToUser(@PathVariable(name = "id") Long id) {
        UserEntity user = userEntityDetailsService.findById(id);
        user.setRole(Role.USER);
        userEntityDetailsService.saveAsUser(user);

        return "redirect:/admin";
    }

    @PostMapping("/user/{id}/change_to_admin")
    public String changeRoleToAdmin(@PathVariable(name = "id") Long id) {
        UserEntity user = userEntityDetailsService.findById(id);
        user.setRole(Role.ADMIN);
        userEntityDetailsService.saveAsUser(user);

        return "redirect:/admin";
    }

    @PostMapping("/search")
    public String searchUser(@RequestBody String searchString) {
        List<UserEntity> userEntityList = userEntityDetailsService.allUsers();
        List<UserEntity> resultList = new ArrayList<>();
        for (UserEntity user : userEntityList) {
            if (user.getUsername().contains(searchString)) {
                resultList.add(user);
            }
        }
        return "redirect:/admin";
    }

}
