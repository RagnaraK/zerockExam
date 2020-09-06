package org.zerock.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeMapper {
	@Select("Select sysdate from DUAL")
	public String getTime();
	
	public String getTime2();
}
