<%-- メニューJSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="bean.Subject, java.util.List" %>
<%
@SuppressWarnings("unchecked")
List<Subject> list=(List<Subject>)request.getAttribute("subject_list");
%>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目管理</h2>
			<a href="SubjectCreate.action">新規登録</a>
			<table>
				<tr>
					<th>科目コード</th>
					<th>科目名</th>
				<tr/>
				<% for(Subject sub: list){ %>
				<tr>
					<td><%= sub.getCd() %></td>
					<td><%= sub.getName() %></td>
					<td><a href="SubjectUpdate.action?cd=<%= sub.getCd()%>">変更</a></td>
					<td><a href="SubjectDelete.action?cd=<%= sub.getCd()%>">削除</a></td>
				<tr/>
				<% } %>
			</table>
		</section>
	</c:param>
</c:import>