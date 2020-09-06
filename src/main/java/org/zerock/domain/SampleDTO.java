package org.zerock.domain;

import lombok.Data;

@Data	// lombok 자동 get/set/equals/toString 생성
public class SampleDTO {
	private String name;
	private int age;
}
