package com.avi6.board.dto;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/*
 * 사용자가 list 에서 페이지 요청시, 페이지 값을 담는 DTO
 * */
@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
	private int page;
	private int size;
	private String type;//조건 검색의 타입(제목 or 작성자)
	private String keyword;
	public PageRequestDTO() {
		//기본값으로 각 값 설정함;
		this.page = 1;
		this.size = 10;
	}
	
	//나중에 Page 처리할 Pageable 리턴 메서드 정의..정렬객체를 파라미터로 받아서 처리함
	public Pageable getPageable(Sort sort) {
		return PageRequest.of(page - 1, size, sort);//페이지는 0부터 시작하기떄문에 위에서 초기값이 1에서 -1 해서 0페이지 리턴
	}
	//Java 의 Function interface 에 대해서 알아보도록 해요.
	
}
