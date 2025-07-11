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
	    <h2>科目管理</h2>
	    <a href="SubjectCreate.action">新規登録</a>
	    <table>
	      <tr>
	        <th>科目コード</th>
	        <th>科目名</th>
	        <th>操作</th>
	      </tr>

	      <%-- ここから空チェック --%>
	      <% if (list == null || list.isEmpty()) { %>
	        <tr>
	          <td colspan="3">科目が登録されていません。</td>
	        </tr>
	      <% } else {
	           for (Subject sub : list) { %>
	        <tr>
	          <td><%= sub.getCd() %></td>
	          <td><%= sub.getName() %></td>
	          <td>
	            <a href="SubjectUpdate.action?cd=<%= sub.getCd() %>">変更</a>
	            &nbsp;|&nbsp;
	            <a href="SubjectDelete.action?cd=<%= sub.getCd() %>">削除</a>
	          </td>
	        </tr>
	      <%   }
	         } %>
	      <%-- ここまで空チェック --%>


	    </table>
	  </section>
	</c:param>
</c:import>