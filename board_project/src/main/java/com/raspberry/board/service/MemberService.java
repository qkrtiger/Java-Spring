package com.raspberry.board.service;

import com.raspberry.board.dao.MemberDao;
import com.raspberry.board.dto.MemberDto;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
@Log
public class MemberService {
    @Autowired
    private MemberDao mDao;

    private ModelAndView mv;

    //비밀번호 암호화를 위한 인코더 객체
    private BCryptPasswordEncoder pEncoder =
            new BCryptPasswordEncoder();

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

    //회원 가입 처리용 메소드
    public String memberJoin(MemberDto member,
                             RedirectAttributes rttr){
        log.info("memberJoin()");
        String view = null;
        String msg = null;

        //비밀번호 암호화 처리
        String encpwd = pEncoder.encode(member.getM_pwd());
        log.info(encpwd);
        //평문인 비밀번호를 암호문으로 덮어씀.
        member.setM_pwd(encpwd);

        try {
            mDao.memberInsert(member);
            view = "redirect:/";
            msg = "가입 성공";
        } catch (Exception e){
            e.printStackTrace();
            view = "redirect:joinForm";
            msg = "가입 실패";
        }
        rttr.addFlashAttribute("msg", msg);

        return view;
    }

    public String loginProc(MemberDto member,
                            HttpSession session,
                            RedirectAttributes rttr){
        log.info("loginProc()");
        String view = null;
        String msg = null;

        //DB에서 회원의 비밀번호 구하기(암호문)
        String encPwd = mDao.selectPass(member.getM_id());
        //encPwd에 담겨있을 수 있는 데이터
        // 1) null : 비회원인 경우
        // 2) 암호화된 비밀번호 문자열 : 회원인 경우
        if(encPwd != null){
            //아이디는 맞음.(회원의 아이디)
            if(pEncoder.matches(member.getM_pwd(), encPwd)){
                //matches 메소드 : Spring Security 에서 제공하는
                //암호문과 평문 비교 메소드.
                //matches(평문, 암호문) 형식으로 작성하면,
                //같은 값일 때 true, 다르면 false를 출력.

                //비밀번호가 맞는 경우
                //세션에 로그인 성공정보(접속자 정보) 저장.
                //  저장할 회원 정보 : id, name, point, g_name
                member = mDao.selectMember(member.getM_id());
                //세션에 DTO 저장.
                session.setAttribute("mb", member);

                //로그인 성공 후 게시판 목록으로 이동.
                //일단 첫페이지로 이동.(나중에 변경할 예정임.)
                view = "redirect:list?pageNum=1";
                msg = "로그인 성공";
            } else {
                //비밀번호가 틀린 경우
                view = "redirect:loginForm";
                msg = "비밀번호가 틀립니다.";
            }
        } else {
            //아이디 없음.(비회원)
            view = "redirect:loginForm";
            msg = "아이디가 존재하지 않습니다.";
        }

        rttr.addFlashAttribute("msg", msg);

        return view;
    }

    public String logout(HttpSession session) {
        log.info("logout()");
        session.invalidate();
        return "redirect:/";//첫페이지로 이동.
    }
}//class end
