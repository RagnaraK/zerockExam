package org.zerock.sample;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Component			// 해당 클래스가 스크링에서 객체로 만들어서 관리하는 대상임을 명시
@ToString			// toString() 메소드 자동생성
@Getter				// 모든 getter 메소드 자동생성
@AllArgsConstructor	// 모든 생성자 자동 생성
public class SampleHotel {
	private Chef chef;
}
