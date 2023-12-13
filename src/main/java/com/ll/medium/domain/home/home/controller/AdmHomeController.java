package com.ll.medium.domain.home.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adm")
public class AdmHomeController {

    @GetMapping("/")
    public String showMain(){
        return "domain/home/home/adm/main";
    }

    @GetMapping("/home/about")
    public String showAbout(){
        return "domain/home/home/adm/about";
    }
}
