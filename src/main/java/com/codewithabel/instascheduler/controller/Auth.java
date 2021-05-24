package com.codewithabel.instascheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class Auth {

    @PostMapping("/auth")
    public String authenticated(@RequestParam(value = "code") String authCode, Model model)
    {
        model.addAttribute("authCode", authCode);
        return "index";
    }

    @GetMapping("/")
    public String index()
    {
        return "index";
    }
}
