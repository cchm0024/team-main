<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="pj" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="nv" tagdir="/WEB-INF/tags/board" %>


<!DOCTYPE html>
<html>
<head>

<%@ include file="/WEB-INF/subModules/bootstrapHeader.jsp"%>

<title>자유게시판</title>

<script>
	$(document).ready(function() {
		$("#list-pagenation1 a").click(function(e) {
			e.preventDefault();

			var actionForm = $("#actionForm");

			actionForm.find("[name=pageNum]").val($(this).attr("href"));

			actionForm.submit();
		});
	});
</script>

</head>
<body>
<pj:navbar />
	
	<div class="container">

		<h4>자유게시판 [${pageMaker.total }]</h4> 
			<table class="table table-striped">
			<thead>
				<tr>
					<th>제목</th>
					<th>작성자</th>
					<th>작성일</th>
					<th>수정일</th>
					<th>추천수</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="board">
					<tr>
						<td>
							<c:url value="/board/get" var="getUrl">
								<c:param name="bno" value="${board.bno }" />
								<c:param name="pageNum" value="${pageMaker.cri.pageNum }" />
								<c:param name="amount" value="${pageMaker.cri.amount }" />
								<c:param name="type" value="${pageMaker.cri.type }" />
								<c:param name="keyword" value="${pageMaker.cri.keyword }" />
							</c:url>
							<a href="${getUrl}"> 
								${board.title } 
								<c:if test="${board.replyCnt > 0 }">
									[${board.replyCnt }] 
								</c:if>
							</a>
						</td>
						<td>${board.writerName }</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.regdate }" /></td>
						<td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.updateDate }" /></td>
						<td><i class="far fa-thumbs-up"></i><span id="like-cnt">${board.like_cnt}</span>&nbsp;&nbsp;<i class="far fa-thumbs-down"></i><span id="dislike-cnt">${board.dislike_cnt}</span></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div>
		<nav aria-label="Page navigation example">
			<ul id="list-pagenation1" class="pagination justify-content-center">

				<c:if test="${pageMaker.prev }">
					<li class="page-item"><a class="page-link"
						href="${pageMaker.startPage - 1 }" tabindex="-1"
						aria-disabled="true">Previous</a></li>
				</c:if>

				<c:forEach begin="${pageMaker.startPage }" end="${pageMaker.endPage }" var="num">
					
					<li class="page-item ${num == cri.pageNum ? 'active' : '' }"><a class="page-link" 
					href="${num }">${num }</a></li>
				</c:forEach>

				<c:if test="${pageMaker.next }">
					<li class="page-item"><a class="page-link"
						href="${pageMaker.endPage + 1 }">Next</a></li>
				</c:if>

			</ul>
		</nav>
	<!--페이지 링크용 -->
		<div style="display:none">
			<form id="actionForm" action="${appRoot }/board/list" method="get">
				<input name="pageNum" value="${cri.pageNum }" /> 
				<input name="amount" value="${cri.amount }" />
				<input name="type" value="${cri.type }" />
				<input name="keyword" value="${cri.keyword }" />
			</form>
		</div>

	</div>

		<div id="board-modal1" class="modal" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title">${messageTitle }</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<p>${messageBody }</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>

	<nv:search /> 
	<pj:footer />
</body>
</html>