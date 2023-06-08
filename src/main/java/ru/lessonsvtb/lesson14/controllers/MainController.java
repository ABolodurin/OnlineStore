package ru.lessonsvtb.lesson14.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping
    public String mainPage() {
        return "redirect:/products";
    }

}
