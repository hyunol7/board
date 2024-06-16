package com.avi6.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avi6.board.dto.ReplyDTO;
import com.avi6.board.service.ReplyService;

import lombok.RequiredArgsConstructor;

/*
 * 리플에 관련된 처리를 하는 컨트롤러 모든 리플에 관련된 res,req 는 REST 형식으로 합니다
 * 때문에 컨트롤러를 선언하는 어노테이션에 @RestController 라고 선언만 하게 되면 끝임
 * 
 * 이후 각 요청을 처리하는 메서드는 매핑 방식을 반드시 Rest 방식에 맞게끔 선언을 해야 하는데
 * 이 부분은 각 모듈(메서드)을 작성할 뗴 띠로 주석
 * 
 * 마지막으로, 2가지를 눈여겨 볼 부분이있는데 
 * 요청을 할 떄 path 를 특정 값만으로만  할 떄가있음 ex> /reply/2 이런식으로
 * 이걸 패스파라미터라고 함, 2라는 값은 요청시마다 변결될 수가 잇음 2, 100, 2 등등
 * 이 뜻은 글 번호를 2번이라고 주겟다는 의미임, 이 자체가 하나의 패스를 구성한다는 의미가 됨
 * 
 * 그럼 컨트롤러에서는 이 패스파라미터를 받도록 해야 하는데, 이때 사용하는 표현법이 {bno} 라는 형식
 * 이렇게 선언하면 패스파람으로 전당되는 요청의 값만 파싱해서 얻어낼 수가 있음
 * 
 * 2. 응답을 할때(서버 -> 클라이언트) 데이터가 만약 json 우로 전달이 되어야 할 경우엔 반드시 응답전에
 * 타입을 먼저 명기해야 함, 그리고 응답하는 객체도 ResponseEntity 라는 객체를 이용함
 * 마지막으로 응답을 줄 때는 반드시 서버가 상태코그(state code)와 같이 넘겨야 하므 그래야 클라이언트 브라우저가
 * 이 해더를 분석 후 데이터를 받을지, 에러코드를 띄울지를 결정하게 됨 
 * 
 */

@RestController
@RequestMapping("/replies/")
@RequiredArgsConstructor
public class ReplyController {
	
	//Service 선언
	private final ReplyService replyService;
	
	//제일 먼저 list 에서 뿌려지는 댓글을 가져오는 기능부터 처리
	//사용자가 게시판을 요청했기 때문에, 요청 path 는 board 로 같이 처리함
	//단 이때 요청되는 댓글의 게시글 정보를 패스파리미터로 같이 보내도록 처리할 예정
	//이 정보를 받아서 댓글을 같이 보내도록 함
	//매핑어노테이션의 속성(value, produces(클라이언트에게 전달된 데이터의 Mime Type 선언)등을 이용해서 요청 및 응답 데이터를 설정함
	//consumes(이건 요청이 올 때 Data Type 을 지정된 타입으로 받겠다는 의미) / produces(보낼 때 지정된 타입으로 보내겠다는 의미)
	@GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
	//jason 으로 보낼때는 반드시 매퍼 메서드의 리턴 타입을 ResponseEntity 로 지정을 해야 함
	//꼭 기억 path 파라미터로 요청이 올 경우엔, 이 파라미터를 받도록 어노테이션을 선언하고, 파라미터 이름을 지정해야 함
	//당연히 변수 타입도 지정해야 함 @PathVariable()
	
	public ResponseEntity<ReplyDTO> getListByBoard(@PathVariable("bno") Long bno){
		
		return new ResponseEntity(replyService.getList(bno), HttpStatus.OK);  
	}
	
	//신규글등록 메서드 정의
	//전송데이터가 JSON,@RequestBody 어노를 이용해서 데이터를 get 합니다.
	@PostMapping("")
	public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){
		System.out.println("신규 댓글 처리요청됨.....");
		Long rno = replyService.register(replyDTO);
		return new ResponseEntity(rno, HttpStatus.OK);

	}
	
	@DeleteMapping("/{rno}")
	public ResponseEntity<String> remove(@PathVariable("rno") Long rno){
		replyService.remove(rno);
		
				return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	
	@PutMapping("/{rno}")
	public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO){
		System.out.println("수정 요청 됨...");
		replyService.modify(replyDTO);
		return new ResponseEntity<String>("success",HttpStatus.OK);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
