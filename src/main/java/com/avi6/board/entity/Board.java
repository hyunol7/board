package com.avi6.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * 이 클래스는 게시판의 주 내용을 담음
 * 
 * 이중 눈 여겨 볼 부분은 작성자 이메일을 참조하는 Member 필드임
 * 
 * 이 필드를 사용하여, insert or select 시 같이 join 이 걸리도록 함 
 * 
 * 테이블 컬럼은 글번호, 글제목, 작성장 정도만 정의
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Board extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long bno; //글번호
	
	private String title;
	
	private String content;
	
	//작성자 필드는 member 객체
	//하위 객체를 부모로, 1 : N 관게로 테이블을 생성 하도록 하는 어노테이션
	//fetch Type 은 default 가 enger 임 ,, 만약 lazy 로 선언시엔, repository 메서드에서 @transaction 을 선언해야 함
	//ToStrong 같은 경우, 참조되는 객체점보까지 나오는데(기본), 이를 재현하는 Anno 가 잇다
	@ManyToOne(fetch = FetchType.LAZY) 
	@ToString.Exclude
	private Member writer;
	
	//수정을 위한 두 개의 setter 만 추가함
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
















