package com.boradgame.family.boardgameinfo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrailingSlashController {
    // Redirect explicit trailing-slash request for boardgames to the controller path without trailing slash
    @GetMapping("/boardgames/")
    public String redirectBoardgamesRoot() {
        return "redirect:/boardgames";
    }

    // Also handle form.html requests that might be requested directly
    @GetMapping("/form.html")
    public String redirectFormHtml() {
        return "redirect:/boardgames/form.html";
    }
}

