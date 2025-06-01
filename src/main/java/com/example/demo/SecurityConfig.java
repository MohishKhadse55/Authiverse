package com.example.demo;

import java.util.Collections;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login").permitAll()
                        .anyRequest().authenticated())
                .saml2Login(saml2 -> saml2
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // <-- Add this for SAML login
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true));

        return http.build();
    }

}
