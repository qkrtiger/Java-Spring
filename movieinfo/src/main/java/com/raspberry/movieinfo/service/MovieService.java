package com.raspberry.movieinfo.service;

import com.raspberry.movieinfo.entity.Movie;
import com.raspberry.movieinfo.repository.MovieRepository;
import com.raspberry.movieinfo.util.PagingUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class MovieService {
    @Autowired
    private MovieRepository mRepo;

    private ModelAndView mv;

    public ModelAndView getMovieList(Integer pageNum,
                                     HttpSession session){
        log.info("getMovieList()");

        if (pageNum == null){
            pageNum = 1;
        }
        int listCnt = 5;//한페이지 당 5개씩 목록 출력

        //페이징 조건 생성(Pageable)
        Pageable pb = PageRequest.of((pageNum - 1), listCnt,
                Sort.Direction.DESC, "mcode");
        //of(시작번호, 목록갯수, 정렬방식, 키필드명)

        Page<Movie> result =
                mRepo.findByMcodeGreaterThan(0L, pb);
        //페이지 형태의 결과를 목록형태로 변환
        List<Movie> mList = result.getContent();

        //전체 페이지
        int totalPage = result.getTotalPages();

        //페이징용 html 작성
        String paging = getPaging(pageNum, totalPage);

        //세션에 페이지 번호 저장
        session.setAttribute("pageNum", pageNum);

        mv = new ModelAndView();
        mv.addObject("mList", mList);
        //페이징용 html 추가
        mv.addObject("paging", paging);

        mv.setViewName("home");

        return mv;
    }

    private String getPaging(Integer pageNum, int totalPage) {
        log.info("getPaging()");
        String pageHtml = null;
        int pageCnt = 2;
        String urlStr = "?";

        PagingUtil paging = new PagingUtil(totalPage, pageNum,
                pageCnt, urlStr);

        pageHtml = paging.makePaging();

        return pageHtml;
    }

    public String insertMovie(List<MultipartFile> files,
                              Movie movie,
                              HttpSession session,
                              RedirectAttributes rttr) {
        log.info("insertMovie()");
        String view = null;
        String msg = null;
        //업로드하는 파일의 이름을 먼저 꺼낸다.
        String upFile = files.get(0).getOriginalFilename();

        try {
            //파일 업로드 처리
            if(!upFile.equals("")) {
                fileUpload(files, session, movie);
            }
            //DB에 영화정보 저장
            mRepo.save(movie);
            //insert, update 모두 save 메소드로 처리
            view = "redirect:/";
            msg = "등록 성공";
        } catch (Exception e){
            e.printStackTrace();
            view = "redirect:writeForm";
            msg = "등록 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    private void fileUpload(List<MultipartFile> files,
                            HttpSession session,
                            Movie movie) throws IOException {
        log.info("fileUpload()");
        String sysname = null;
        String oriname = null;

        String realPath = session.getServletContext()
                .getRealPath("/");
        realPath += "upload/";
        File folder = new File(realPath);
        if(folder.isDirectory() == false){
            folder.mkdir();
        }

        MultipartFile mf = files.get(0);
        oriname = mf.getOriginalFilename();

        sysname = System.currentTimeMillis()
                + oriname.substring(oriname.lastIndexOf("."));

        File file = new File(realPath + sysname);
        mf.transferTo(file);

        movie.setMoriname(oriname);
        movie.setMsysname(sysname);
    }

    public ModelAndView getMovie(Long mcode) {
        log.info("getMovie()");
        mv = new ModelAndView();

        Movie movie = mRepo.findById(mcode).get();
        mv.addObject("movie", movie);

        return mv;
    }

    public String updateMovie(List<MultipartFile> files,
                              Movie movie,
                              HttpSession session,
                              RedirectAttributes rttr) {
        log.info("updataMovie()");
        String view = null;
        String msg = null;
        String upFile = files.get(0).getOriginalFilename();
        String poster = movie.getMsysname();

        try {
            if(!upFile.equals("")){
                fileUpload(files, session, movie);
            }
            mRepo.save(movie);

            //기존 파일 삭제
            if(poster != null && !upFile.equals("")){
                //기존 파일이 있고, 새파일이 들어왔을 때
                fileDelete(poster, session);
            }

            view = "redirect:detail?mcode="
                    + movie.getMcode();
            msg = "수정 성공";
        } catch (Exception e){
            e.printStackTrace();
            view = "redirect:updateForm?mcode="
                    + movie.getMcode();
            msg = "수정 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    private void fileDelete(String poster,
                            HttpSession session) throws Exception {
        log.info("fileDelete()");
        String realPath = session.getServletContext()
                .getRealPath("/");
        realPath += "upload/" + poster;
        File file = new File(realPath);
        if(file.exists()){
            file.delete();
        }
    }
}//class end
