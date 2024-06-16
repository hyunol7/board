package com.avi6.board.dto;
/*
 * 게시판의 글 내용을 담을 DTO
 * 
 * Entity 에는 Member 를 참조하고 있는데, 실제 게시판에는 Member 의 email 정도만 사용예정
 * 또 하나는 list 페이지에는 몇 개의 댓글이 존재하는 지를 나타냄, 이를 위해
 * 
 * getWithBno(Long bno) 의 Query 에 countQuery 를 정의해서 댓글수도 가지고 왓음..
 * 
 * 위 두 개는 각각 member, Reply 의 정보에 속하지만, 전체 정보가 아니기 때문에,
 * 
 * 따로 객체 선언은 하지 않고 필드로 선언해서 필요한 정보만 매핑할 예쩡
 * 
 */

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

	private Long bno;
	
	private String title;
	
	private String content;
	
	private String writeEmail; //member entity 에서 get 후 매핑 예정
	
	private String writerName;
	
	private LocalDateTime regDate;
	
	private LocalDateTime modDate;
	
	private int replyCount; //댓글 수 속성
	
	
}
