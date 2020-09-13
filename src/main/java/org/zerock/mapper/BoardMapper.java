package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {

	public List<BoardVO> getList();

	public List<BoardVO> getListWithPaging(Criteria cri);

	public void insert(BoardVO board);

	public Integer insertSelectKey(BoardVO board);

	public BoardVO read(Long bno);

	public int delete(Long bno);

	public int update(BoardVO board);

	public int getTotalCount(Criteria cri);
	// 파라미터로 받을수 있도록 처리, 2개이상 데이터를 전달하기 위해서 @Param 처리
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
}
