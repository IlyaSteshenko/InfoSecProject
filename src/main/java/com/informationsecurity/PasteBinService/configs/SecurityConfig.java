package com.informationsecurity.PasteBinService.configs;

import com.informationsecurity.PasteBinService.models.UserEntityDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserEntityDetailsService userEntityDetailsService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((request) -> {
                    request.requestMatchers("/create_new_paste").hasAnyAuthority("USER", "ADMIN");
                    request.requestMatchers("/admin", "/registration_admin").hasAuthority("ADMIN");
                    request.requestMatchers("/registration", "/**").permitAll();
                })
                .formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userEntityDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

}
