package com.raspberry.movieinfo_practice.controller;

import com.raspberry.movieinfo_practice.service.MovieService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class MovieController {
    private ModelAndView mv;

    @Autowired
    private MovieService mServ;

    @GetMapping("/")
    public ModelAndView home(Integer pageNum, HttpSession session){
        log.info("home()");

        mv = mServ.getMovieList(pageNum, session);
        mv.setViewName("home");
        return mv;

    }
}
