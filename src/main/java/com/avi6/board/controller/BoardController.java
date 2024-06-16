package com.avi6.board.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.avi6.board.dto.BoardDTO;
import com.avi6.board.dto.PageRequestDTO;
import com.avi6.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board/")
@RequiredArgsConstructor //서비스 주입 어노테이션
public class BoardController {
	
	//서비스 이용해서 CURD 를 해야하니, 필드 선언,, 자동주입받아야 하므로 반드시 private final 로 할것
	private final BoardService boardService;
	
	
	   //글 삭제 
	   @PostMapping("/remove")
	   public String delete(BoardDTO boardDTO, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,@RequestParam("bno") Long bno, Model model, RedirectAttributes redirectAttributes) {
		   
		   System.out.println("글 삭제 번호 : " + bno);
			
	      boardService.delArticle(bno);
	      
	      redirectAttributes.addAttribute("bno", boardDTO.getBno());
	      
	      return "redirect:/board/list";
	   }
	
	
	//글수정 실행 매핑.. 수정된 후 글 번호가 있는 페이지로 Redirect 시킴
	@PostMapping("/modify")
	public String modify(BoardDTO boardDTO, 
			@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, 
			RedirectAttributes redirectAttributes) {
		
		System.out.println("글 수정 요청 : " + boardDTO);
		
		boardService.updateArticle(boardDTO);
		
		//넘겨준 page 넘버등을 생성후 리타이랙트 함
		redirectAttributes.addAttribute("page",pageRequestDTO.getPage());
		
		redirectAttributes.addAttribute("bno", boardDTO.getBno());
		
		return "redirect:/board/read";
	}
	
	
	//글수정폼 매핑
	@GetMapping("/modify")
	public void modify(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,@RequestParam("bno") Long bno, Model model) {
		System.out.println("글 수정 요청 번호 : " + bno);
		BoardDTO boardDTO = boardService.get(bno);
		model.addAttribute("dto",boardDTO);
	}
	
	//글상세 매핑
	@GetMapping("/read")
	public void read(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,@RequestParam("bno") Long bno, Model model) {
		System.out.println("글상세 요청번호 : " + bno);
		
		BoardDTO boardDTO = boardService.get(bno);
		
		model.addAttribute("dto",boardDTO);
	}
	
	//새글작성 폼 매핑 요청 처리
	@GetMapping("/register")
	public void register() {
		System.out.println("새 글 작성 요청처리함");
	}
	
	//신규글 저장처리
	@PostMapping("/register")
	public String register(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
		
		Long bno = boardService.register(boardDTO);
		
		redirectAttributes.addAttribute("msg", "신규글" + bno + " 가 정상 등록되었음");
		return "redirect:/board/list";
		
	}
	
	@GetMapping("/")
	public String list() {//viewer 를 리턴하도록 string 리턴 메서드 작성
		return "redirect:/board/list";
	}
	
	//list get 요청 매핑
	@GetMapping("/list")
	public void list(PageRequestDTO pageRequestDTO, Model model) {
		//리스트페이지의 항목을 모델에 실어서 viewer 넘김
		model.addAttribute("result", boardService.getList(pageRequestDTO));
		
	}

}
