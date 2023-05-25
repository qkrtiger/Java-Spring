package com.raspberry.board.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class SessionInterceptor
        implements AsyncHandlerInterceptor {
    //컨트롤러로 요청(request)이 전달되기 전에 처리
    //로그인 전에 처리하는 메소드
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler)
            throws Exception {
        log.info("preHandle()");

        //세션의 로그인 정보가 있는지 확인하기 위해 session 구하기.
        HttpSession session = request.getSession();

        if(session.getAttribute("mb") == null){
            //로그인을 안한 상태로 페이지 접근
            log.info("인터셉트! - 로그인 안함");

            response.sendRedirect("/");
            return false;
        }
        return true;
    }

    //로그아웃 후에 처리하는 메소드
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView)
            throws Exception {
        log.info("postHandle()");
        //현재 사용하는 웹 프로토콜(http) 버전은 1.1과 1.0 혼용
        if(request.getProtocol().equals("HTTP/1.1")){
            response.setHeader("Cache-Control",
                    "no-cache, no-store, must-revalidate");
        }
        else {
            response.setHeader("Pragma", "no-cache");
        }
    }
}
