package org.zerock.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class TodoDTO {
	private String title;
	@DateTimeFormat(pattern = "yyyy/MM/dd")	// pattern 형식을 Date 객체로 전달, 사용시 InitBinder 불필요
	private Date dueDate;
}
