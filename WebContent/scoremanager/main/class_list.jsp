<%-- クラス管理JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">
		クラス管理
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">クラス管理</h2>
			<div class="text-end mb-3 pe-3">
				<a href="ClassCreate.action">新規登録</a>
			</div>

			<table class="table">
				 <thead>
				 	<tr>
				 		<th>クラス番号</th>
				 		<th></th>
			 		</tr>
			 	</thead>
			 	<tbody>
			 		<c:forEach var="classNum" items="${classNumList}">
			 			<tr>
			 				<td>${classNum}</td>
			 				<td>
			 					<a href=>変更</a>
			 				</td>
			 			</tr>
			 		</c:forEach>
			 	</tbody>
			 </table>
		</section>
	</c:param>
</c:import>