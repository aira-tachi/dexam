<%-- 成績登録完了JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="content">
		<div id="wrap_box">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2">学生情報登録</h2>
			<div id="wrap_box">
				<p class="text-center" style="background-color:#66CC99; margin-bottom: 120px;">登録が完了しました</p>
				<a href="StudentCreate.action" class="me-5">戻る</a>
				<a href="StudentList.action">学生一覧</a>
			</div>
		</div>
	</c:param>
</c:import>

