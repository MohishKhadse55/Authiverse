package com.example.demo;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication.getPrincipal() instanceof Saml2AuthenticatedPrincipal samlPrincipal) {
            model.addAttribute("name", samlPrincipal.getName());
            model.addAttribute("emailAddress", samlPrincipal.getFirstAttribute("email"));
            model.addAttribute("userAttributes", samlPrincipal.getAttributes());
        } else if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            model.addAttribute("name", oidcUser.getFullName());
            model.addAttribute("emailAddress", oidcUser.getEmail());
            model.addAttribute("userAttributes", oidcUser.getAttributes());
        }

        return "home";
    }
}
