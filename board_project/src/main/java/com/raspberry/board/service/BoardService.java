package com.raspberry.board.service;

import com.raspberry.board.dao.BoardDao;
import com.raspberry.board.dao.MemberDao;
import com.raspberry.board.dto.*;
import com.raspberry.board.util.PagingUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Service
@Slf4j
public class BoardService {
    @Autowired
    private BoardDao bDao;
    @Autowired
    private MemberDao mDao;

    private ModelAndView mv;
    private int lcnt = 5;//목록에 보여질 게시글 개수(초기 5개)

    public ModelAndView getBoardList(SearchDto sdto,
                                     HttpSession session){
        log.info("getBoardList()");
        mv = new ModelAndView();

        //SQL 쿼리문의 limit 부분 설정.
        int num = sdto.getPageNum();
        //출력할 게시물 수가 설정되지 않으면 기본값(5)로 설정
        if(sdto.getListCnt() == 0){
            sdto.setListCnt(lcnt);
        }
        //페이지번호를 limit 시작 번호로 변경
        sdto.setPageNum((num - 1) * sdto.getListCnt());

        //Dao로 게시글 목록 가져오기
        List<BoardDto> bList = bDao.selectBoardList(sdto);

        mv.addObject("bList", bList);

        //페이징 처리
        sdto.setPageNum(num);
        String pageHtml = getPaging(sdto);
        mv.addObject("paging", pageHtml);

        //세션에 필요 정보 저장(pageNum, 검색관련 정보)
        //페이지 번호 저정 - 글쓰기 또는 상세보기 화면에서
        //목록으로 돌아갈 때
        session.setAttribute("pageNum", num);
        //검색 결과 목록으로 돌아갈 때
        if(sdto.getColname() != null){
            //검색 결과 목록
            session.setAttribute("sdto", sdto);
        } else {
            //검색이 아닐 때는 SearchDto를 제거
            //세션에 저장한 데이터 삭제 : removeAttribute()
            session.removeAttribute("sdto");
        }

        mv.setViewName("boardList");
        return mv;
    }

    private String getPaging(SearchDto sdto) {
        String pageHtml = null;

        //전체 글개수 구하기.
        int maxNum = bDao.selectBoardCnt(sdto);
        //페이지에 보여질 번호 개수
        int pageCnt = 5;
        String listName = null;
        if(sdto.getColname() != null){
            listName = "list?colname=" + sdto.getColname()
                    + "&keyword=" + sdto.getKeyword() + "&";
        }//list?colname=b_title&keyword=4&
        else {
            listName = "list?";
        }

        PagingUtil paging = new PagingUtil(maxNum,
                sdto.getPageNum(),
                sdto.getListCnt(),
                pageCnt, listName);

        pageHtml = paging.makePaging();

        return pageHtml;
    }

    public String boardWrite(List<MultipartFile> files,
                             BoardDto board,
                             HttpSession session,
                             RedirectAttributes rttr){
        log.info("boardWrite()");
        String view = null;
        String msg = null;

        try {
            //글 내용 저장.
            bDao.insertBoard(board);
            log.info("게시글 번호 : " + board.getB_num());

            //파일 업로드 처리
            fileUpload(files, session, board);

            //작성한 회원의 point 증가 처리(session)
            MemberDto member = (MemberDto) session.getAttribute("mb");
            int point = member.getM_point() + 10;
            if(point > 100){//100 초과하지 않도록
                point = 100;
            }
            member.setM_point(point);
            //DB member 테이블 수정(update)
            mDao.updateMemberPoint(member);
            session.setAttribute("mb", member);
            //같은 이름으로 세션에 저장하면 덮어쓰기가 된다.

            //목록 첫페이지로 이동.
            view = "redirect:list?pageNum=1";
            msg = "글 작성 성공";
        } catch (Exception e){
            e.printStackTrace();
            view = "redirect:writeForm";
            msg = "글 작성 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    private void fileUpload(List<MultipartFile> files,
                            HttpSession session,
                            BoardDto board)
            throws Exception {
        log.info("fileUpload()");
        //파일 저장 위치
        String realPath = session.getServletContext()
                .getRealPath("/");
        realPath += "upload/";//업로드용 폴더 : upload
        File folder = new File(realPath);
        if(folder.isDirectory() == false){
            folder.mkdir();//폴더 생성.
        }

        //파일 저장 처리(목록이므로 반복 처리)
        for(MultipartFile mf : files){
            //파일명(원래 이름) 추출
            String orname = mf.getOriginalFilename();
            if(orname.equals("")){
                return;
            }

            //파일 정보 저장
            BfileDto bf = new BfileDto();
            bf.setBf_bnum(board.getB_num());//게시글번호
            bf.setBf_oriname(orname);//원래 파일 이름
            String sysname = System.currentTimeMillis()
                    + orname.substring(orname.lastIndexOf("."));
            bf.setBf_sysname(sysname);

            //파일 저장(to upload folder)
            File file = new File(realPath + sysname);
            mf.transferTo(file);

            //파일 정보 저장(DB)
            bDao.insertFile(bf);
        }//for end
    }//method end

    public ModelAndView getBoard(Integer b_num){
        log.info("getBoard()");
        mv = new ModelAndView();

        //글 내용 가져오기
        BoardDto board = bDao.selectBoard(b_num);

        //파일 목록 가져오기
        List<BfileDto> fList = bDao.selectFiles(b_num);

        //댓글 목록 가져오기
        List<ReplyDto> rList = bDao.selectReply(b_num);

        //조회수 1증가(수정)

        //가져온 데이터를 mv에 담기
        mv.addObject("board", board);
        mv.addObject("fList", fList);
        mv.addObject("rList", rList);

        //mv에 view 지정하기
        mv.setViewName("boardContents");

        return mv;
    }

    public ResponseEntity<Resource> fileDownload(
                    BfileDto bfile, HttpSession session)
            throws IOException {
        log.info("fileDownload()");
        //파일 저장 경로
        String realpath = session.getServletContext()
                .getRealPath("/");
        realpath += "upload/" + bfile.getBf_sysname();

        //실제 파일이 저장된 하드디스크까지 경로를 수립.
        InputStreamResource fResource =
                new InputStreamResource
                        (new FileInputStream(realpath));

        //파일명이 한글인 경우의 처리(UTF-8로 인코딩)
        String fileName = URLEncoder
                .encode(bfile.getBf_oriname(), "UTF-8");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + fileName)
                .body(fResource);
    }

    public ReplyDto replyInsert(ReplyDto reply) {
        log.info("replyInsert()");
        try {
            //1. DB에 댓글 insert
            bDao.insertReply(reply);
            //2. 삽입한 바로 그 댓글을 select
            reply = bDao.selectLastReply(reply.getR_num());
        } catch (Exception e){
            e.printStackTrace();
            reply = null;
        }

        return reply;
    }

    public String boardDelete(Integer b_num,
                              HttpSession session,
                              RedirectAttributes rttr){
        log.info("boardDelete()");
        String view = null;
        String msg = null;

        try {
            //댓글/파일정보 -> 게시글
            bDao.deleteReplys(b_num);
            //실제 파일들 삭제
            deleteFiles(b_num, session);
            //파일 정보 삭제
            bDao.deleteFiles(b_num);
            bDao.deleteBoard(b_num);

            view = "redirect:list?pageNum=1";
            msg = "삭제 성공";
        } catch (Exception e){
            e.printStackTrace();
            view = "redirect:contents?b_num=" + b_num;
            msg = "삭제 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    private void deleteFiles(Integer b_num,
                             HttpSession session)
            throws Exception {
        //sysname으로 upload 폴더에서 파일 삭제.
        //DB에서 같은 게시물에 해당하는 sysname 목록을
        //받아서 반복적으로 삭제 처리.
        //file.delete() : File 클래스의 파일 삭제 메소드.
        log.info("deleteFiles()");
        List<String> sList = bDao.selectSysname(b_num);

        String realpath = session.getServletContext()
                .getRealPath("/");
        realpath += "upload/";

        for(String sn : sList){
            File file = new File(realpath + sn);
            if(file.exists()){
                file.delete();
            }
        }
    }

    public ModelAndView updateForm(Integer b_num){
        log.info("updateForm()");
        //게시글 내용 가져오기
        BoardDto board = bDao.selectBoard(b_num);
        //파일 목록 가져오기
        List<BfileDto> fList = bDao.selectFiles(b_num);

        //mv에 추가.
        mv = new ModelAndView();
        mv.addObject("board", board);
        mv.addObject("fList", fList);

        //view 지정
        mv.setViewName("updateForm");
        return mv;
    }

    public String updateBoard(List<MultipartFile> files,
                              BoardDto board,
                              HttpSession session,
                              RedirectAttributes rttr){
        log.info("updateBoard()");
        String view = null;
        String msg = null;

        try {
            bDao.updateBoard(board);
            fileUpload(files, session, board);
            view = "redirect:contents?b_num="
                    + board.getB_num();
            msg = "수정 성공";
        } catch (Exception e){
            e.printStackTrace();
            view = "redirect:updateForm?b_num="
                    + board.getB_num();
            msg = "수정 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    public List<BfileDto> fileDelete(String sysname,
                                     Integer bnum,
                                     HttpSession session){
        log.info("fileDelete()");
        List<BfileDto> fList = null;

        //파일 삭제를 위한 실제 경로 구하기
        String realpath = session.getServletContext()
                .getRealPath("/");
        realpath += "upload/" + sysname;

        try {
            //파일 삭제
            File file = new File(realpath);
            if(file.exists()){
                if(file.delete()){
                    //파일 정보 삭제(파일 삭제 성공 시)
                    bDao.deleteSingleFile(sysname);
                    //나머지 파일 목록 다시 가져오기
                    fList = bDao.selectFiles(bnum);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return fList;
    }
}//class end
