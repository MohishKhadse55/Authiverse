package com.example.demo.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SessionConfig implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
    }
}
