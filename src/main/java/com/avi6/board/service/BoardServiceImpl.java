package com.avi6.board.service;



import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import com.avi6.board.dto.BoardDTO;
import com.avi6.board.dto.PageRequestDTO;
import com.avi6.board.dto.PageResultDTO;
import com.avi6.board.entity.Board;
import com.avi6.board.entity.Member;
import com.avi6.board.repository.BoardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	//모든 Repository 는 private final 로 선언하고 어노테이션을 이용해서 객체 주입을 받는다.
	private final BoardRepository boardRepository;
	@Override
	public Long register(BoardDTO boardDto) {
		
		System.out.println("전달될 DTO --> " + boardDto);
		
		//DTO --> Entity 변환
		Board board = dtoTOEntity(boardDto);
		
		boardRepository.save(board);
		
		return board.getBno();
	}
	
	@Override
	public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDto){
		
		//이 메서드는 List 를 추출하는 repo 의 메서드 호출
		//이떄 사용자의 요청 페이지와 페이비 전호, 갯수등은 모두 pageRequestDTO 에 담겨짐
		//이 정보를 이용해서 요청된 페이지 번호, 글 목록, page index 도 함꼐 넘겨야하는데
		//이를  처리한 클래스가 PageResultDTO 
		//PageResultDTO 객체를 생성해서 넘겨주면 위 정보가 모두 계산되어져서 필드에 매핑 되어지기 때문에
		//viewer 에서는 메서드 호출만으로 페이징, 번호, 글목록등을 모두 표현 가능함
		//하지만, 제네릭으로 BoardDTO 의 objext[] 를 파람으로 받기 때문에 
		//객체를 생성할 떄 object 배열을 boardDTO 로 변환하는 기능(함수(function)) 이 필요함 
		//이 기능은 service 인터페이스에서 entityToDTO(param) 으로 정의 햇기 떄문에
		
		System.out.println("게시판 요청 list 요청 처리 --> " + pageRequestDto);
		
		//Entity --> DTO 로 변환하는 Function 객체를 생성해서 PageResult 객체의 생성자로 넘겨야 합니다.
		//또한 사용자가 요청한 페이지의 페이지 정보(페이지번호, 목록수 등..)을 처리한 boardRepo 의 get...()
		//를 호출해서 처리된 Page 객체도 얻어냅니다
		//위 두 개를 pageResult 객체의 생성자로 넘김녀서 처리된 내용을 담은 객체를 얻어냅니다.
		//repository 에는 2개의 Entity(Board,, Member) 와, 리플 갯수를 담은 3개의 값이 각 Row 에
		//배열로 담겨있기 때문에, 이 값을 DTO 에 변환하기 위해서는 위 세 개의 값을 분리해서 호출해 줘야 합니다.
		//java.util.Function interface 사용법
		//T,R 타입을 제네릭으로 선언해서, 이를 구현한 실제 메서드의 구현체를 대입해 주면 됩니다
		
		Function<Object[], BoardDTO> converFunction = 
				(entity -> entityToDto((Board)entity[0], (Member)entity[1], (Long)entity[2]));
		
		//위에서 Function 객체를 생성했으니,  이번엔 Page 객체를 얻어냄
		Page<Object[]> result =
				boardRepository.getBoardWithReplyCnt(pageRequestDto.getPageable(Sort.by("bno").descending()));
				
		
		//PageResult 객체를 생성하기 위한 생성자 Param 2개를 모두 만들었으니 PageResult 객체를 생성해서
		//페이지 정보, entity --> DTO 로 변환된 목록, 그리고 reple 갯수를 모두 담은 PaggeResult 객체를 생성해서 넘깁니다.
		return new PageResultDTO<>(result, converFunction);
		
	}
	
	@Override
		public BoardDTO get(Long bno) {
			System.out.println("글 상세 요청함,, 글 번호 -->" + bno);
			
			Object obj = boardRepository.getBoardWithBno(bno);
			
			Object[] arr = (Object[])obj;
			
			return entityToDto((Board)arr[0], (Member)arr[1], (Long)arr[2]);
		}

	@Override
	public void delArticle(Long bno) {
		//댓글부터 삭제
		boardRepository.delByBno(bno);
		
		//제목글 삭제는 JPA
		boardRepository.deleteById(bno);
	}
	
	@Override
	@Transactional
	public void updateArticle(BoardDTO boardDTO) {
		//dto 의 수정된 내용을 바로 save 함
		//이렇게 하면, 원본 Entity(Bean Proxy --> Bean 을 관리하는 메모리)가 변경됨을 인지하고 update 처리함
		Board board = boardRepository.getReferenceById(boardDTO.getBno());
		
		board.setTitle(boardDTO.getTitle());
		board.setContent(boardDTO.getContent());
		
		boardRepository.save(board);
	}
		
	
	}


