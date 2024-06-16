package com.avi6.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avi6.board.entity.Board;

import jakarta.transaction.Transactional;

public interface BoardRepository extends JpaRepository<Board, Long> {
	
	//글삭제 메서드 정의
	@Modifying //JPQL 의 update / delete 시엔 반드시 선언해야함
	@Transactional//하나 이상의 세션이 필요한 부분에는 꼭 넣어야함
	@Query("Delete from Reply r where r.board.bno =:bno")
	void delByBno(@Param("bno") Long bno);
	
	

	//특정 게시물의 정보를 얻어내도록 JPQL 을 이용해서 사용하는 방법 알아보기
	//하나의 게시물에는 0 ~ n 개 까지의 댓글들이 같이 나올 수도 있습니다
	//이런결우엔 하나의 Row(게시글) 에 할당된 댓글들이 모두 object[] 로 리턴받도록 함
	//주의 메서드에서 리턴되는 객체는 object 이고, 이 내부의 실제 row 가 object[] 로 생성합니다.
	//JPQL
	// =: 표시는 우측의 값을 Parameter 로 처리하겟다는 의미
	//이런 경우에는 메서드에서 해당 이름을 @Param("bno") Long bno(변수) 처리해서 동적으로 값을 매핑시킨다
	//하위 쿼리는 Board Table 에서 참조 관계에 있는 Writer 정보만 뽑아옵니다
	//때문에 on 을 이용하지 않고 조인을 걸어도 수행이됩니다. 믈론 on 을 걸어도 상관 없음
	@Query("SELECT b,w From Board b LEFT OUTER JOIN b.writer w where b.bno =:bno")
	Object getBoardWriter(@Param("bno") Long bno);
	
	//특정 게시물과 그에 속한 댓글을 가져오는 JPQL 을 작성하려함
	//하위 쿼리는 리턴이 댓글 목록을 object 배열값으로 갖는 list 로 리턴함
	@Query("SELECT b,r From Board b LEFT OUTER JOIN Reply r ON r.board = b where b.bno =:bno")
	List<Object[]> getBoardWithReply(@Param("bno") Long bno);
	
	
	//리스트 페이지에서 pagenation 을 처리할 떄 사용할 JPQL 작성
	//이 쿼리는 리스트 페이지에서 하나의 제목글에 댓글의 갯수를 담아서 표시하고, 댓글이 있는 항목을 클릭하게 되면 
	//댓글이 보여지게 만듦
	//떄문에, 하나의 게시글에는 몇 개의 댓글이 있는지를 파악해야 하고, 두 번째로는 전체 글수를 알아야 
	//페이징 처리를 할 떄, pageable 을 이용할 수 있음
	//그런데, 현재 JPQL 을 사용하기 때문에, 반드시 집계함수 쿼리를 따로 작성을 해야 함
	//이른 countQuery 라고 함
	//해서,  여기에서는 그룹별로 게시글을 묶고(group by), 그리고, 게시글의 수가 몇 개인지를 count 하는 쿼리를 모두 작성합니다.
	//이렇게 하는 문법 : (value="mainQuery", countQuery="집계에 사용될 쿼리")로 정의
	//사용될 테이블 갯수? 
	@Query(value = "SELECT b,w,COUNT(r) "
			+ "FROM Board b " 
			+ "LEFT JOIN b.writer w " 
			+ "LEFT JOIN Reply r ON r.board = b " 
			+ "GROUP BY b ",
		countQuery = "SELECT COUNT(b) FROM Board b")
	Page<Object[]> getBoardWithReplyCnt(Pageable pageable);
	
	
	//특정 bno 를 파라미터로 받아서 board, writer, count(reply) 의 모든 내용을 리턴시키도록(Object 리턴)
	// JPQL 메서드 선언 , 메서드 명은 getBoardWithBno
	
	@Query("SELECT b,w,COUNT(r) FROM Board b LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b Where b.bno = :bno") 
	Object getBoardWithBno(@Param("bno") Long bno);
	
	
	
	
	
	
	
	
	
	
	
	
	
}
