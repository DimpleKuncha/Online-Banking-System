package com.banking.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection for testing (not recommended for production)
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/accounts/**  ").permitAll() // Permit all requests to this endpoint
                        .anyRequest().permitAll()
                );

        return http.build();
    }

}
