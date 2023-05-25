package com.raspberry.board.controller;

import com.raspberry.board.dto.MemberDto;
import com.raspberry.board.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private MemberService mServ;

    @GetMapping("/")
    public String home(){
        //log.info("home()");
        log.info("home()");
        return "home";
    }

    @GetMapping("joinForm")
    public String joinForm(){
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

    @PostMapping("joinProc")
    public String joinProc(MemberDto member,
                           RedirectAttributes rttr){
        log.info("joinProc()");
        String view = mServ.memberJoin(member, rttr);

        return view;
    }

    @GetMapping("loginForm")
    public String loginForm(){
        log.info("loginForm()");
        return "loginForm";
    }

    @PostMapping("loginProc")
    public String loginProc(MemberDto member,
                            HttpSession session,
                            RedirectAttributes rttr){
        log.info("loginProc()");
        String view = mServ.loginProc(member, session, rttr);
        return view;
    }

    @GetMapping("logout")
    public String logout(HttpSession session){
        log.info("logout()");
        String view = mServ.logout(session);
        return view;
    }
}//class end
