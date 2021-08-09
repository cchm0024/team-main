<%@ page  contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="pj" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<style>
div.A {
text-align: center;
}
</style>
<%@ include file="/WEB-INF/subModules/bootstrapHeader.jsp" %>


<title>찾아오시는 길</title>
</head>
<body>
<pj:navbar />
<div class="container">
	<h2>찾아오시는 길</h2>
			<div style="background-color: rgb(238, 238, 238); height:600px; vertical-align:top;">
			  	<a href="https://choongang-eemin90.s3.ap-northeast-2.amazonaws.com/help/lee2min+map.png" target="_blank">
			  	<img src="https://choongang-eemin90.s3.ap-northeast-2.amazonaws.com/help/lee2min+map1.png" width="720" height="450" title="위의 이미지를 클릭하면 확대됩니다." >
			  	</a>
			  	<div class="A"><b>서울 강남구 테헤란로 7길 7 ESCO 빌딩 7층 4실습실</b><br>
			  	<span style="color:blue;">(강남역 12번 출구방향 200m 전진 후 좌측으로 50m 12층 건물)</span></div> 	
			</div>	
	<nav class="nav flex-column">
  
  
</nav>
</div>
<pj:footer/>
</body>
</html>