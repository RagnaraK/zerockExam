<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload with Ajax</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
	//원본 이미지를 보여줄 <div> 처리, <a>태그에서 showImage()를 호출할 수 있는 방식으로 작성을 위해 ready밖에 작성
	function showImage(fileCallPath){
		//alert(fileCallPath);
		$(".bigPictureWrapper").css("display","flex").show();
		
		$(".bigPicture").html("<img src='/display?fileName=" + encodeURI(fileCallPath) + "'>").animate({width:'100%',height:'100%'}, 1000);
	}

	$(document).ready(function(){
		// 정규표현식을 이용한 파일 확장자 및 크기 사전 처리
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
		var maxSize = 5242880;	// 5MB
		
		function checkExtension(fileName, fileSize){
			if(fileSize >= maxSize){
				alert("파일 사이즈 초과");
				return false;
			}
			
			if(regex.test(fileName)){
				alert("해당 종류의 파일은 업로드할 수 없습니다");
				return false;
			}
			return true;
		}
		
		var cloneObj = $(".uploadDiv").clone();	// 첨부파일 업로드 전 내용없는 <input file>객체 포함 복사
		
		// upload Ajax 처리
		$("#uploadBtn").on("click", function(e){
			var formData = new FormData();	// Ajax를 이용한 파일 업로드
			
			var inputFile = $("input[name='uploadFile']");
			var files = inputFile[0].files;
			console.log(files);
			
			//add filedate to formdata
			for(var i=0;i<files.length;i++){
				if(!checkExtension(files[i].name, files[i].size)) return false;	// 파일 검사
				formData.append("uploadFile", files[i]);
			}
			
			$.ajax({
				//url:'uploadAjaxAction',	// Ajax 업로드 파일 처리
				//url:'uploadAjaxFolderAction',	// 년/월/일 폴더 생성 처리
				//url:'uploadAjaxUUIDAction',	// 중복방지를 위한 UUID 적용 처리
				//url : 'uploadThumbnail',	// 이미지 판단 및 썸네일 생성 저장 처리
				url : 'uploadAttachDTO',	// 이미지 데이터 반환 처리
				processData: false,		// 전송시 false
				contentType: false,
				data: formData,
				type: 'post',
				dataType: 'json',
				success: function(result){
					console.log(result);
					showUploadedFile(result);
					$(".uploadDiv").html(cloneObj.html());	// 첨부파일 업로드 후 복사된 객체 다시 추가하여 첨부파일 부분 초기화
				}
			}); //$.ajax Upload
		});
		
		// 파일 썸네일 출력 및 클릭시 다운로드
		var uploadResult = $(".uploadResult ul");
		
		function showUploadedFile(uploadResultArr){
			var str = "";
			
			$(uploadResultArr).each(function(i, obj){
				if(!obj.image){
					var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid+"_"+obj.fileName);
					var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
					str += "<li><div><a href='/download?fileName="+fileCallPath +"'>" + "<img src='/resources/img/attach.png'>"+ obj.fileName +"</a>"
							+"<span data-file=\'"+ fileCallPath+"\' data-type='file'>x </span></div></li>";
				}
				else{
					//str +="<li>" + obj.fileName +"</li>";	// 파일 이름 출력
					var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid+"_"+obj.fileName);
					var originPath = obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName;
					originPath = originPath.replace(new RegExp(/\\/g),"/");
					
					str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\"><img src='/display?fileName="+fileCallPath +"'></a>"
							+"<span data-file=\'"+fileCallPath+"\' data-type='image'>x</span></li>";	// 썸네일 출력
					
				}
			});
			
			uploadResult.append(str);
		}
		
		// 원본 이미지 사라지는 이벤트 처리
		$(".bigPictureWrapper").on("click", function(e){
			$(".bigPicture").animate({width:'0%',height:'0%'},1000);
			setTimeout(() =>{$(this).hide();},1000);	// ES6의 화살표 함수
			// IE 11
			/* setTimeout(function(){
				$(".bigPictureWrapper").hide();
			},1000); */
		});
		
		// x버튼 이벤트 처리, 이벤트 위임 방식
		$(".uploadResult").on("click","span",function(e){
			var targetFile = $(this).data("file");
			var type = $(this).data("type");
			console.log(targetFile);
			
			$.ajax({
				url : '/deleteFile',
				data : {fileName : targetFile, type:type},
				dataType : 'text',
				type:'post',
				success:function(result){
					alert(result);
				}
			});
		});
	});
</script>
<style type="text/css">
	/* 썸네일 이미지 스타일 처리 */
	.uploadResult{
		width:100%;
		background-color:gray;
	}
	.uploadResult ul{
		display:flex;
		flex-flow:row;
		justify-content: center;
		align-items:center;
	}
	.uploadResult ul li{
		list-style: none;
		padding:10px;
	}
	.uploadResult ul li img{
		width:20px;
	}
	.uploadResult ul li span{
		color:white;
	}
	/* 썸네일 클릭시 큰 이미지 처리 */
	.bigPictureWrapper{
		position: absolute;
		display:none;
		justify-content: center;
		align-items:center;
		top:0%;
		width: 100%;
		height:100%;
		background-color: gray;
		z-index:100;
		background:rgba(255,255,255,0.5);
	}
	.bigPicture{
		position:relative;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.bigPicture img{
		width:600px;
	}
</style>
</head>
<body>
	<h1>Upload with Ajax</h1>
	
	<div class="uploadDiv">
		<input type="file" name="uploadFile" multiple>
	</div>
	<div class="uploadResult">
		<ul></ul>
	</div>
	<button id="uploadBtn">Upload</button>
	
	<div class="bigPictureWrapper">
		<div class="bigPicture"></div>
	</div>
	
	
</body>
</html>