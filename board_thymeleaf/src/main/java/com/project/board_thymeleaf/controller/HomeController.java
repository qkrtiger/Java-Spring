package com.project.board_thymeleaf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home(){
        log.info("home()");
        return "home";
    }

    @GetMapping("joinForm")
    public String joinForm(){
        log.info("joinForm()");
        return "joinForm";
    }

    @PostMapping("joinProc")
    public String joinProc(){
        log.info("joinProc()");
        String view = null;

        return view;
    }
}
