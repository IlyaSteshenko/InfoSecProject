package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.UserEntity;
import com.informationsecurity.PasteBinService.models.UserEntityDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

    @Autowired
    private SecurityContextRepository securityContextRepository;

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }

    public void authorizeUser(
            UserEntity user,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
                .getContextHolderStrategy();

        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        user.getAuthorities()
                )
        );
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

}
