package com.informationsecurity.PasteBinService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.annotation.SessionScope;

@RequiredArgsConstructor
@Service
@SessionScope
public class ModelContextProviderService {
    private final SecurityContextService securityContextService;
    private final UserEntityDetailsService userEntityDetailsService;

    public void addHeaderDependencies(Model model){
        String userName = securityContextService.getUsername();
        model
                .addAttribute("authorities", securityContextService.getAuthorities())
                .addAttribute("userName", userName);
        if (!userName.equals("anonymousUser")) {
            model.addAttribute(
                    "user",
                    userEntityDetailsService.loadUserByUsername(userName));
        }
    }
}
