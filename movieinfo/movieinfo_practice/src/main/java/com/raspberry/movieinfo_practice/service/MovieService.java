package com.raspberry.movieinfo_practice.service;

import com.raspberry.movieinfo.util.PagingUtil;
import com.raspberry.movieinfo_practice.entity.Movie;
import com.raspberry.movieinfo_practice.repository.MovieRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
@Slf4j
public class MovieService {
    @Autowired
    private MovieRepository mRepo;

    private ModelAndView mv;

    public ModelAndView getMovieList(Integer pageNum, HttpSession session){
        log.info("getMovieList()");

        if(pageNum == null){
            pageNum =1;
        }
        int listCnt = 5; //한 페이지당 5개씩 목록 출력

        //페이징 조건 생성(Pageable)
        Pageable pb = PageRequest.of((pageNum -1), listCnt, Sort.Direction.DESC, "mcode");
        //of(시작번호, 목록개수, 정렬방식, 키필드명)

        Page<Movie> result = mRepo.findByMcodeGreaterThan(0L, pb);

        //페이지 형태의 결과를 목록형태로 변환
        List<Movie> mList = result.getContent();

        //전체 페이지
        int totalPage = result.getTotalPages();

        //페이징용 html 작성 //getPaging 서비스 메소드 생성
        String paging = getPaging(pageNum, totalPage);

        //세션에 페이지 번호 저장
        session.setAttribute("pageNum", pageNum);

        mv = new ModelAndView();
        mv.addObject("mList", mList);

        //페이징용 html 추가
        mv.addObject("paging", paging);

        //뷰네임 지정
        mv.setViewName("home");


        return mv;
    }

    //페이징 구간 설정
    private String getPaging(Integer pageNum, int totalPage) {
        log.info("getPaging()");
        String pageHtml = null;
        int pageCnt = 2;
        String urlStr = "?";

        PagingUtil paging = new PagingUtil(totalPage, pageNum, pageCnt, urlStr);

        pageHtml = paging.makePaging();

        return pageHtml;
    }
}
