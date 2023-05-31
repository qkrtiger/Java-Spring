package com.raspberry.jpaprj.service;

import com.raspberry.jpaprj.entity.JpaData;
import com.raspberry.jpaprj.repository.JpaDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
@Slf4j
public class JpaDataService {
    @Autowired
    private JpaDataRepository jRepo;

    private ModelAndView mv;

    public ModelAndView getList(){
        log.info("getList()");
        mv = new ModelAndView();
        mv.setViewName("home");
        //목록 추가 작업
        List<JpaData> jList = jRepo.findAll();
        //findAll() -> SELECT * FROM table
        mv.addObject("jList", jList);

        return mv;
    }

    public String insertData(JpaData data){
        log.info("insertData()");
        String view = null;

        try{
            jRepo.save(data);//insert/update
            view = "redirect:/";
        } catch (Exception e){
            e.printStackTrace();
            view = "redirect:writeForm";
        }
        return view;
    }

    public ModelAndView getData(String keyword) {
        log.info("getData()");
        mv = new ModelAndView();
        mv.setViewName("home");
        List<JpaData> jList = jRepo.findByStrdata(keyword);
        mv.addObject("jList", jList);
        return mv;
    }
}//class end
