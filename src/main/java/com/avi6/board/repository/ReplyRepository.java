package com.avi6.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avi6.board.entity.Board;
import com.avi6.board.entity.Reply;

import jakarta.transaction.Transactional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
	
	//게시물 삭제 선언
	//아래 쿼리는 제목글 댓글삭제 쿼리
	@Modifying
	@Transactional
	@Query("delete from Reply r where r.board.bno =:bno")
	void deleteByBno(@Param("bno")Long bno);
	
	//List 페이지에서 댓글을 보여줄 댓글 list 메서드 선언
	List<Reply> getRepliesByBoardOrderByRno(Board board); //쿼리 메서드
}
