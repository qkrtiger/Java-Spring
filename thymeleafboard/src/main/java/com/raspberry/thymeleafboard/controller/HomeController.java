package com.raspberry.thymeleafboard.controller;

import com.raspberry.thymeleafboard.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private MemberService mServ;

    @GetMapping("/")
    public String home(){
        log.info("home()");
        return "home";
    }

    @GetMapping("joinForm")
    public String joinFrom(){
        log.info("joinForm()");
        return "joinForm";
    }

    //REST 방식(Ajax)으로 전송할 경우의 메소드
    @GetMapping("idCheck")
    @ResponseBody           //REST 방식일 때 꼭 넣을 것.
    public String idCheck(String mid){
        log.info("idCheck()");
        String res = mServ.idCheck(mid);

        return res;
    }

}//class end
