package com.avi6.board.service;
/*
 * 선언할 부분
 * 
 * 1. 댓글등록 기능
 * 2. 댓글 List get 기능
 * 2. 댓글 수정 및 삭제 기능
 * 4. Reply --> Entity 변화 default 메서드
 * 5. 위의 반대 메서드
 */

import java.util.List;

import com.avi6.board.dto.ReplyDTO;
import com.avi6.board.entity.Board;
import com.avi6.board.entity.Reply;

public interface ReplyService {

	//DTO --> Entity 변환 메서드 정의
	default Reply dtoToEntity(ReplyDTO dto) {
		Board board  = Board.builder().bno(dto.getBno()).build();
		
		Reply reply = Reply.builder()
				.rno(dto.getRno())
				.text(dto.getText())
				.replyer(dto.getReplyer())
				.board(board)
				.build();
		
		return reply;
	}
	
	//Entity --> DTO : 참조 객체의 Board 전체는 필요치 않으니 게시물 번호만 처리함
	default ReplyDTO entityToDto(Reply reply) {
		
		ReplyDTO replyDTO = ReplyDTO.builder()
				.rno(reply.getRno())
				.text(reply.getText())
				.replyer(reply.getReplyer())
				.regDate(reply.getRegDate())
				.modDate(reply.getModDate())
				.build();
		
		return replyDTO;
	}
	
	Long register(ReplyDTO replyDTO);
	List<ReplyDTO> getList(Long bno); //댓글 목록 getter
	void modify(ReplyDTO replyDTO); //수정
	void remove(Long rno); //삭제
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
