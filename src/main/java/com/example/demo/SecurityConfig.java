package com.example.demo;

import java.util.Collections;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.ldap.core.support.LdapContextSource;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/", "/login").permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form.loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll(false))
                                .saml2Login(saml2 -> saml2
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true) // <-- Add this for SAML login
                                )
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true));

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(LdapAuthenticationProvider ldapProvider) {
                return new ProviderManager(ldapProvider);
        }

        @Bean
        public LdapAuthenticationProvider ldapProvider() {
                BindAuthenticator authenticator = new BindAuthenticator(contextSource());
                authenticator.setUserDnPatterns(new String[] { "cn={0},dc=maxcrc,dc=com" }); // adjust as needed

                DefaultLdapAuthoritiesPopulator authoritiesPopulator = new DefaultLdapAuthoritiesPopulator(
                                contextSource(), "ou=groups");
                authoritiesPopulator.setIgnorePartialResultException(true);

                return new LdapAuthenticationProvider(authenticator, authoritiesPopulator);
        }

        @Bean
        public LdapContextSource contextSource() {
                LdapContextSource contextSource = new LdapContextSource();
                contextSource.setUrl("ldap://localhost:389");
                contextSource.setBase("dc=maxcrc,dc=com");
                contextSource.setUserDn("cn=Manager,dc=maxcrc,dc=com");
                contextSource.setPassword("password@123");
                return contextSource;
        }

}
