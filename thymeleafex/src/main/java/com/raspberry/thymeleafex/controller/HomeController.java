package com.raspberry.thymeleafex.controller;

import com.raspberry.thymeleafex.dto.PersonDto;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {
    @GetMapping("/")
    public String home(Model model){
        log.info("home()");
        //문자열 전송
        model.addAttribute("d1" , "서버로부터 <b>전송</b>된 메시지");
        String str = "결과 메시지";
        int num = 300;
        model.addAttribute("d2", str);
        model.addAttribute("d3", num);

        PersonDto person = new PersonDto();
        person.setPname("홍길동");
        person.setAge(20);
        person.setPhone("010-1234-5678");
        model.addAttribute("pe", person);

        return "home";
    }

    @GetMapping("second")
    public ModelAndView second(HttpSession session){
        log.info("second()");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("second");

        //로그인을 성공했다면(가정)
        session.setAttribute("id", "user01");



        return mv;
    }

    @GetMapping("logout")
    public String logout(RedirectAttributes rttr){
        log.info("logout()");
        String view = "redirect:/";
        String msg = "로그아웃";
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    @GetMapping("datain")
    public String datain(String data1, Integer data2){
        log.info("datain()");
        log.info("data1 : " + data1);
        log.info("data2 : " + data2);

        return "second";
    }

    @PostMapping("pinput")
    public String pinput(PersonDto person){
        log.info("pinput()");
        log.info("이름 : " + person.getPname());
        log.info("나이 : " + person.getAge());
        log.info("연락처 : " + person.getPhone());

        return "second";
    }

    @GetMapping("third")
    public String third(Model model){
        log.info("third()");
        PersonDto per = new PersonDto();
        per.setPname("손흥민");
        per.setAge(30);
        per.setPhone("010-2222-3333");
        model.addAttribute("person", per);
        return "third";
    }

    @GetMapping("fourth")
    public ModelAndView fourth(){
        log.info("fourth()");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("fourth");
        mv.addObject("id", "hong01");
        mv.addObject("age", 37);

        List<PersonDto> pList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            PersonDto per = new PersonDto();
            per.setPname("사람" + i);
            per.setAge(20 + i);
            per.setPhone("010-1234-567" + i);
            pList.add(per);
        }
        mv.addObject("pList", pList);

        return mv;
    }
}//class end
