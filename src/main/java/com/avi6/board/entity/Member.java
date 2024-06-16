package com.avi6.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{
	
	//회원정보는 email(pk), password, name
	
	@Id
	private String email;//pk 문자열로 처리할 거라 따로 전략 없음
	
	private String password;
	
	private String name;

}
