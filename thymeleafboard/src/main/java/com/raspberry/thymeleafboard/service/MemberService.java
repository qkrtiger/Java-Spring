package com.raspberry.thymeleafboard.service;

import com.raspberry.thymeleafboard.dao.MemberDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
@Slf4j
public class MemberService {
    @Autowired
    private MemberDao mDao;

    private ModelAndView mv;

    //아이디 중복 확인 메소드
    public String idCheck(String m_id){
        //리턴 타입과 같은 변수를 먼저 선언할 것.
        String res = null;

        //아이디가 중복이면 1, 아니면 0
        int cnt = mDao.idCheck(m_id);

        if(cnt == 0){
            res = "ok";
        } else {
            res = "fail";
        }
        return res;
    }
}
