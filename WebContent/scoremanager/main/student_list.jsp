<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
<c:param name="title">学生管理</c:param>

  <c:param name="scripts">
<!-- 必要なJSがあればここに記述 -->
</c:param>

  <c:param name="content">
<section class="me-4">
<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生管理</h2>

      <!-- フィルタフォーム -->
<form action="StudentList.action" method="get" class="px-4">
<div class="row mb-3">
<div class="col-md-3">
<label for="entYear" class="form-label">入学年度</label>
<select name="ent_year" id="entYear" class="form-select">
<option value="">--------</option>
<c:forEach var="year" items="${entYearList}">
<option value="${year}" <c:if test="${entYear == year}">selected</c:if>>${year}</option>
</c:forEach>
</select>
</div>

<div class="col-md-3">
<label for="classNum" class="form-label">クラス</label>
<select name="class_num" id="classNum" class="form-select">
<option value="">--------</option>
<c:forEach var="cls" items="${classList}">
<option value="${cls}" <c:if test="${classNo == cls}">selected</c:if>>${cls}</option>
</c:forEach>
</select>
</div>

<div class="col-md-3 d-flex align-items-end">
<div class="form-check">
<input class="form-check-input" type="checkbox" name="isAttend" value="on" id="isAttend"
<c:if test="${param.isAttend == 'on'}">checked</c:if> />
<label class="form-check-label" for="isAttend">在学中</label>
</div>
</div>

<div class="col-md-3 d-flex align-items-end">
<button type="submit" class="btn btn-primary">絞り込み</button>
</div>
</div>
</form>

<div class="px-4 mb-3">
<a href="StudentCreate.action">新規登録</a>
</div>

      <div class="px-4 mb-3">

        検索結果：${students.size()}件
</div>

<c:if test="${empty students}">
<div class="px-4">学生情報が存在しませんでした</div>
</c:if>

<c:if test="${not empty students}">
<div class="px-4">
<table class="table">
<thead>
<tr>
<th>入学年度</th>
<th>学生番号</th>
<th>氏名</th>
<th>クラス</th>
<th>在学中</th>
<th></th>
</tr>
</thead>
<tbody>
<c:forEach var="student" items="${students}">
<tr>
<td>${student.entYear}</td>
<td>${student.no}</td>
<td>${student.name}</td>
<td>${student.classNum}</td>
<td>
<c:choose>
<c:when test="${student.attend}">○</c:when>
<c:otherwise>×</c:otherwise>
</c:choose>
</td>
<td>
<a href="StudentUpdate.action?studentNo=${student.no}">変更</a>
</td>
</tr>
</c:forEach>
</tbody>
</table>
</div>
</c:if>
</section>
</c:param>
</c:import>