package com.informationsecurity.PasteBinService.configs;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final Cache<String, Integer> loginAttemptsCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException {

        String ip = request.getRemoteAddr();
        int attempts = loginAttemptsCache.get(ip, k -> 0) + 1;
        loginAttemptsCache.put(ip, attempts);

        if (attempts >= 5) { // Блокировка после 5 попыток
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("Too many login attempts. Try later.");
        } else {
            response.setStatus(401); // Unauthorized
            response.sendRedirect(request.getContextPath() + "/login?error");
        }
    }
}