package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

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
                authenticator.setUserDnPatterns(new String[] { "cn={0}" }); // adjust as needed

                // DefaultLdapAuthoritiesPopulator authoritiesPopulator = new
                // DefaultLdapAuthoritiesPopulator(
                // contextSource(), "ou=groups");
                // authoritiesPopulator.setIgnorePartialResultException(true);

                return new LdapAuthenticationProvider(authenticator);
        }

        @Bean
        public LdapContextSource contextSource() {
                LdapContextSource contextSource = new LdapContextSource();
                contextSource.setUrl("ldap://9.46.245.207:15389");
                contextSource.setBase("dc=company,dc=com");
                contextSource.setUserDn("cn=root");
                contextSource.setPassword("password@123");
                return contextSource;
        }

}
