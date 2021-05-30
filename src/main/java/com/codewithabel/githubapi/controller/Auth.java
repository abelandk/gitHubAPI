package com.codewithabel.githubapi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Auth {

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal, Model model) {

        model.addAttribute("user_name", principal.getAttribute("name"));
        return "index";
    }

    @GetMapping("/")
    public String index() {

        return "login";
    }
}
