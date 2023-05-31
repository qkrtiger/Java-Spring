package com.raspberry.jpaprj.controller;

import com.raspberry.jpaprj.entity.JpaData;
import com.raspberry.jpaprj.service.JpaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private JpaDataService jServ;

    private ModelAndView mv;

    @GetMapping("/")
    public ModelAndView home(){
        log.info("home()");
        mv = jServ.getList();
        return mv;
    }

    @GetMapping("writeForm")
    public String writeForm(){
        log.info("writeForm()");
        return "writeForm";
    }

    @PostMapping("dataProc")
    public String dataProc(JpaData data){
        log.info("dataProc()");
        String view = jServ.insertData(data);

        return view;
    }

    @GetMapping("search")
    public ModelAndView search(String keyword){
        log.info("search()");
        mv = jServ.getData(keyword);

        return mv;
    }
}//class end
