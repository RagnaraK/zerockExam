package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	// form 방식 파일 업로드 처리
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form");
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		String uploadFolder = "D:\\upload";
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("------------------------------------");
			log.info("Upload File getContentType() : " + multipartFile.getContentType());
			log.info("Upload File getOriginalFilename() : " + multipartFile.getOriginalFilename());	// 파일이름.확장자 출력
			log.info("Upload File getName() : " + multipartFile.getName());
			log.info("Upload File getSize() : " + multipartFile.getSize());
			log.info("Upload File getResource() : " + multipartFile.getResource());
			
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());	// (폴더, 파일이름);
			
			try {
				log.info("Upload File getBytes() : " + multipartFile.getBytes());
				log.info("Upload File getInputStream() : " + multipartFile.getInputStream());
				multipartFile.transferTo(saveFile);	// 파일 경로 저장
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	// Ajax 파일 업로드 처리(확장자 및 크기 사전 체크 처리 jsp에서 사전 처리)
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload Ajax");
	}
	
	@PostMapping("/uploadAjaxAction")
	public void uploadAjaxPost(MultipartFile[] uploadFile) {	// Ajax 처리시 Model 불필요
		log.info("upload ajax post..................");
		String uploadFolder = "D:\\upload";
		
		for(MultipartFile multipartFile : uploadFile){
			log.info("-----------------------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File Size : " + multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			// IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("only file name : " + uploadFileName);
			
			File saveFile = new File(uploadFolder, uploadFileName);
			
			try {
				multipartFile.transferTo(saveFile);
			} catch (IllegalStateException | IOException e) {
				log.error(e.getMessage());
			}
		}
	}
	
	// 중복된 이름의 첨부파일 처리
	// 하나의 폴더에 파일이 너무 많을 경우 속도저하와 개수의 제한 문제가 생기는 것을 방지
	@PostMapping("/uploadAjaxFolderAction")
	public void uploadAjaxFolderPost(MultipartFile[] uploadFile) {
		String uploadFolder = "D:\\upload";
		
		// make folder
		File uploadPath = new File(uploadFolder, getFolder());
		log.info("upload path : " + uploadPath);
		
		if(uploadPath.exists() == false) {	// 폴더 존재 여부 검사
			uploadPath.mkdirs();		//make yyyy/MM/dd forlder
		}
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("---------------------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File Size : " + multipartFile.getSize());
			

			String uploadFileName = multipartFile.getOriginalFilename();
			
			// 파일명과 확장자까지만 잘라내기, IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("only file name : " + uploadFileName);
			
			File saveFile = new File(uploadPath, uploadFileName);
			try {
				multipartFile.transferTo(saveFile);
			} catch (IllegalStateException | IOException e) {
				log.error(e.getMessage());
			}
			
		}
	}
	
	// UUID를 활용한 파일 이름 중복 방지 처리
	@PostMapping("/uploadAjaxUUIDAction")
	public void uploadAjaxUUIDPost(MultipartFile[] uploadFile) {
		String uploadFolder = "D:\\upload";
		
		// make folder
		File uploadPath = new File(uploadFolder, getFolder());
		log.info("upload path : " + uploadPath);
		
		if(uploadPath.exists() == false) {	// 폴더 존재 여부 검사
			uploadPath.mkdirs();		//make yyyy/MM/dd forlder
		}
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("---------------------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File Size : " + multipartFile.getSize());
			

			String uploadFileName = multipartFile.getOriginalFilename();
			
			// 파일명과 확장자까지만 잘라내기, IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("only file name : " + uploadFileName);
			
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			log.info("UUID file name : " + uploadFileName);
			
			File saveFile = new File(uploadPath, uploadFileName);
			try {
				multipartFile.transferTo(saveFile);
			} catch (IllegalStateException | IOException e) {
				log.error(e.getMessage());
			}
			
		}
	}
	
	@PostMapping("/uploadThumbnail")
	public void uploadThumbnail(MultipartFile[] uploadFile) {
		String uploadFolder = "D:\\upload";
		// make folder
		File uploadPath = new File(uploadFolder, getFolder());
		log.info("upload path : " + uploadPath);
		
		if(uploadPath.exists() == false) uploadPath.mkdirs();	// yyyy/MM/dd 폴더 생성
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("-------------------------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File Size : " + multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			log.info("UUID file name : " + uploadFileName);
						
			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile);
				if(checkImageType(saveFile)) {
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				}				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	// 업로드 파일 데이터 반환
	@ResponseBody
	@PostMapping(value = "/uploadAttachDTO", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AttachFileDTO>> uploadAttachDTOPost(MultipartFile[] uploadFile){
		List<AttachFileDTO> list = new ArrayList<AttachFileDTO>();
		String uploadFolder = "D:\\upload";
		String uploadFolderPath = getFolder();
		
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("upload path : " + uploadPath);
		
		if(uploadPath.exists() == false) uploadPath.mkdirs();	// yyyy/MM/dd 폴더 생성
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("-------------------------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File Size : " + multipartFile.getSize());
			
			AttachFileDTO attachDTO = new AttachFileDTO();
						
			String uploadFileName = multipartFile.getOriginalFilename();
			// 폴더 잘라내기
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			attachDTO.setFileName(uploadFileName);	// UUID의 값을 쓰기전에 파일 이름 저장
			// 파일 이름 랜덤생성
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			log.info("UUID file name : " + uploadFileName);
			
			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile);
				
				// DTO 저장
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);				
				// 이미지 여부 체크
				if(checkImageType(saveFile)) {
					attachDTO.setImage(true);
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				}
				list.add(attachDTO);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return new ResponseEntity<List<AttachFileDTO>>(list, HttpStatus.OK);
	}
	
	// 년/월/일 폴더의 생성
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}
	
	// 이미지 파일 여부 판단(Ajax의 경우 브라우저만을 통해서 들어오는것은 아니기 때문에 확인 필요
	private boolean checkImageType(File file) {
		try {
			String contentType= Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	// 섬네일 데이터 전송하기(경로나 파일 이름에 한글 혹은 공백 등의 문자는 문제가 발생할 수 있으므로 JavaScript의 encodeURIComponet()함수를 이용해서 URI에 문제가 없는 문자열을 생성해서 처리
	// 특정한 파일 이름을 받아서 이미지 데이터를 전송하는 코드
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){	// 파일 경로가 포함된 fileName 파라미터를 받음
		log.info("fileName : " + fileName);
		
		File file = new File("D:\\upload\\" + fileName);
		
		log.info("file : " + file);
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath()));	// probeContentType을 이용하여 브라우저에 적절한 MIME타입 데이터를 Http의 헤더 메시지에 포함 처리 
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
		
	}
	
	// 일반 첨부파일 다운로드 처리 : (byte[]로 처리가 가능하나, springframework.core.io.Resource 타입을 이용하여 간단히 처리, 한글처리
	@GetMapping(value="/download", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(String fileName, @RequestHeader("User-Agent") String userAgent){	// 헤더에서 브라우저 종류 확인 파리미터 @RequestHeader("User-Agent")
		log.info("download file : " + fileName);
		Resource resource = new FileSystemResource("D:\\upload\\" + fileName);
		if(resource.exists() == false) return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		
		String resourceName = resource.getFilename();
		
		// UUID 파일 이름 삭제
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") +1);	// 원본파일 이름 형태로 짤라내기
		HttpHeaders headers = new HttpHeaders();
		
		try {
			String downloadName = null;
			if(userAgent.contains("Trident")) {	// IE
				log.info("IE borwser");
				downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8").replace("\\+", " ");
			}else if(userAgent.contains("Edge")) {	//Edge
				log.info("Edge browser");
				downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8");
				log.info("Edge Name : " + downloadName);
			}else {
				log.info("Chrome browse");
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"),"ISO8859-1");
			}
			
			headers.add("Content-Disposition", "attachment; filename=" + downloadName);
			//headers.add("Content-Disposition", "attachment; filename=" + new String(resourceName.getBytes("UTF-8"), "ISO-8859-1"));	// 한글인 경우 저장시 깨지는 문제를 막기 위해 문자열 처리,Content-Disposition는 IE에서 한글깨짐 발생(인코딩방식이 다름)
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	// 첨부파일 삭제 처리
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type){
		log.info("deleteFile : " + fileName);
		
		File file;
		
		try {
			file = new File("D:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));
			file.delete();
			if(type.equals("image")) {	// 이미지인 경우 원본이미지도 삭제 처리
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				log.info("largeFileName" + largeFileName);
				file = new File(largeFileName);
				
				file.delete();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}

}
