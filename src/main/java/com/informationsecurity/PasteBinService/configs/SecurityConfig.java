package com.informationsecurity.PasteBinService.configs;

import com.informationsecurity.PasteBinService.services.UserEntityDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserEntityDetailsService userEntityDetailsService;
    private final UserEntityDetailsService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            SecurityContextRepository securityContextRepository
    ) throws Exception {
        httpSecurity
                .csrf(CsrfConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                        var corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        corsConfiguration.setAllowCredentials(true);
                        return corsConfiguration;
                }))
                .authorizeHttpRequests((request) -> {
                    request.requestMatchers(
                            "/create_new_paste",
                            "/new_password",
                            "/change_principals"
                    ).hasAnyAuthority("USER", "ADMIN");
                    request.requestMatchers(
                            "/admin/**",
                            "/registration_admin"
                    ).hasAuthority("ADMIN");
                    request.requestMatchers(
                            "/registration",
                            "/**",
                            "/profile/**"
                    ).permitAll();
                    request.requestMatchers(
                            "/resources/**",
                            "/static/**",
                            "/css/**",
                            "/img/**",
                            "/js/**"
                    ).permitAll();
                })
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .sessionManagement(manager -> manager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .securityContext(context -> context
                        .securityContextRepository(securityContextRepository));

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userEntityDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

//    @Bean
//    public void addResourceHandler(ResourceHandlerRegistry registry) {
//        registry
//                .addResourceHandler("/")
//                .addResourceLocations("");
//    }

}
