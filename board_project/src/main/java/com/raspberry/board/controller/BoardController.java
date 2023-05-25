package com.raspberry.board.controller;

import com.raspberry.board.dto.BfileDto;
import com.raspberry.board.dto.BoardDto;
import com.raspberry.board.dto.ReplyDto;
import com.raspberry.board.dto.SearchDto;
import com.raspberry.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class BoardController {
    @Autowired
    private BoardService bServ;

    private ModelAndView mv;

    @GetMapping("list")
    public ModelAndView boardList(SearchDto sdto,
                                  HttpSession session){
        log.info("boardList()");
        mv = bServ.getBoardList(sdto, session);//서비스에서 데이터 삽입 및 목적페이지 지정.
        return mv;
    }

    @GetMapping("writeForm")
    public String writeForm(){
        log.info("writeForm()");
        return "writeForm";
    }

    //멀티파트 데이터를 처리하는 메소드의 첫번째 매개변수는
    //Multipart 파일 목록(List)여야 한다.
    //List 앞에 @RequestPart 어노테이션을 붙인다.
    @PostMapping("writeProc")
    public String writeProc(@RequestPart List<MultipartFile> files,
                            BoardDto board, HttpSession session,
                            RedirectAttributes rttr){
        log.info("writeProc()");
        String view = bServ.boardWrite(files, board, session, rttr);
        return view;
    }

    @GetMapping("contents")
    public ModelAndView boardContents(Integer b_num){
        log.info("boardContents()");
        mv = bServ.getBoard(b_num);
        return mv;
    }

    @GetMapping("download")
    public ResponseEntity<Resource> fileDownload(
                                    BfileDto bfile,
                                    HttpSession session)
            throws IOException {
        log.info("fileDownload()");
        ResponseEntity<Resource> resp =
                bServ.fileDownload(bfile, session);
        return resp;
    }

    @PostMapping("replyInsert")
    @ResponseBody
    public ReplyDto replyInsert(ReplyDto reply){
        log.info("replyInsert()");
        reply = bServ.replyInsert(reply);
        return reply;
    }

    @GetMapping("delete")
    public String boardDelete(Integer b_num,
                              HttpSession session,
                              RedirectAttributes rttr){
        log.info("boardDelete()");
        String view = bServ.boardDelete(b_num, session, rttr);
        return view;
    }

    @GetMapping("updateForm")
    public ModelAndView updateForm(Integer b_num){
        log.info("updateForm()");
        mv = bServ.updateForm(b_num);
        return mv;
    }

    @PostMapping("updateProc")
    public String updateProc(@RequestPart List<MultipartFile> files,
                             BoardDto board,
                             HttpSession session,
                             RedirectAttributes rttr){
        log.info("updateProc()");
        String view = bServ.updateBoard(files, board, session, rttr);
        return view;
    }

    @PostMapping("delFile")
    @ResponseBody
    public List<BfileDto> delFile(String sysname,
                                  Integer bnum,
                                  HttpSession session){
        log.info("delFile()");
        List<BfileDto> fList = bServ.fileDelete(sysname, bnum, session);

        return fList;
    }
}//class end
