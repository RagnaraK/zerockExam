package org.zerock.controller;

import java.util.ArrayList;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.SampleDTO;
import org.zerock.domain.SampleDTOList;
import org.zerock.domain.TodoDTO;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/sample/*")
@Log4j
public class SampleController {
	
	@RequestMapping("")	// localhost:8080/sample/
	public void basic() {
		log.info("basic..................");
	}
	@RequestMapping(value="/basic", method= {RequestMethod.GET})	// method를 통해 여러방식 가능
	public void basicGet() {
		log.info("basic get ...................");
	}
	@GetMapping("/basicOnlyGet")	// Only Get 방식
	public void basicPost() {
		log.info("basic Only Get ...................");
	}
	
	@GetMapping("/ex01")
	public String ex01(SampleDTO dto) {
		log.info("instance parameter type : " + dto);
		return "ex01";
	}
	
	@GetMapping("ex02")	// 기본형으로 받는데 파라미터로 사용된 변수의 이름과 전달되는 파라미터 이름이 다른 경우 유용
	public String ex02(@RequestParam("name") String name, @RequestParam("age") int age) {
		log.info("ex02 : name = " + name);
		log.info("ex02 : age = " + age);
		return "ex02";
	}
	
	@GetMapping("/ex02List")	// ?ids=111&ids=222&ids=333
	public String ex02List(@RequestParam("ids") ArrayList<String> ids) {
		log.info("ids : " + ids);
		return "ex02List";
	}
	
	@GetMapping("/ex02Bean")	// 프로젝트 경로 특수문자 유의
	public String ex02Bean(SampleDTOList list) {
		log.info("list dtos : " + list);
		return "ex02Bean";
	}
	
	/*@InitBinder
	public void initBinder(WebDataBinder binder) {
		// yyyy-MM-dd -> java.util.Date 형식으로 바인딩
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, false));
	}*/
	
	@GetMapping("/ex03")
	public String ex03(TodoDTO todo) {
		log.info("todo : " + todo);
		return "ex03";
	}
	
	@GetMapping("/ex04")// SampleDTO는 Java Bean 규칙 적용 자동 view 전달(앞글자는소문자로 전달), 기본자료형은 안됨
	public String ex04(SampleDTO dto, @ModelAttribute("page") int page) {
		log.info("ex04 dto : " + dto);
		log.info("ex04 page : " + page);
		return "/sample/ex04";
	}
	
	@GetMapping("/ex05")
	public void ex05() {	// 해당URL경로 그대로 jsp 파일 이름 사용
		log.info("/ex05...........");
	}
	
	@GetMapping("/ex06")	// 리턴 객체 타입
	public @ResponseBody SampleDTO ex06() {
		log.info("/ex06 ..................");
		SampleDTO dto = new SampleDTO();
		dto.setAge(10);
		dto.setName("홍길동");
		return dto;
	}
	
	@GetMapping("/ex07")
	public ResponseEntity<String> ex07(){
		log.info("/ex07..............");
		// {"name" : "홍길동"}
		String msg = "{\"name\" : \"홍길동\"}";
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		return new ResponseEntity<String>(msg, header,HttpStatus.OK);
	}
	
	@GetMapping("/exUpload")
	public void exUpload() {
		log.info("/exUpload------------------");
	}
	
	@PostMapping("/exUploadPost")
	public void exUploadPost(ArrayList<MultipartFile> files) {
		files.forEach(file ->{
			log.info("------------------------------");
			log.info("name : " + file.getOriginalFilename());
			log.info("size : " + file.getSize());
		});
	}
}

