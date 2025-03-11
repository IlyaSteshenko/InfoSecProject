package com.informationsecurity.PasteBinService.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }

}
