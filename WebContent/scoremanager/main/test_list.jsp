<%-- 成績参照JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">
		成績参照
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>
			<div class="border p-3 mb-3 rounded">

				<%-- 科目別検索フォーム --%>
				<form action="TestListSubjectExecute.action" method="post" class="d-flex align-items-center flex-wrap">
				 	<input type="hidden" name="type" value="subject">
					<div class="d-flex flex-wrap gap-3 p-1">
						<p class="mb-0 me-5 align-self-center">科目情報</p>
						<%-- 入学年度 --%>
						<div class="d-flex flex-column me-2" style="width: 140px;">
							<label class="form-label">入学年度</label>
							<select name="f1" id="f1" class="form-select">
								<%-- 初期値は"--------" --%>
								<option value="">--------</option>
								<c:forEach var="year" items="${entYears}">
									<option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
								</c:forEach>
							</select>
						</div>

						<%-- クラス --%>
						<div class="d-flex flex-column me-2" style="width: 140px;">
							<label class="form-label">クラス</label>
							<select name="f2" id="f2" class="form-select">
								<option value="">--------</option>
								<c:forEach var="classNum" items="${classNums}">
									<option value="${classNum}" <c:if test="${classNum == f2}">selected</c:if>>${classNum}</option>
								</c:forEach>
							</select>
						</div>

						<%-- 科目 --%>
						<div class="d-flex flex-column me-2" style="width: 280px;">
							<label class="form-label">科目</label>
							<select name="f3" id="f3" class="form-select">
								<option value="">--------</option>
								<c:forEach var="subject" items="${subjects}">
									<option value="${subject.cd}" <c:if test="${subject.cd == f3}">selected</c:if>>${subject.name}</option>
								</c:forEach>
							</select>
						</div>

						<%-- 検索ボタン --%>
						<div class="align-self-center ms-4">
							<button type="submit" class="btn btn-secondary">検索</button>
						</div>
					</div>
				</form>
				<hr class="my-3">

				<%-- 学生別検索フォーム --%>
				<form action="TestListStudentExecute.action" method="post" class="d-flex align-items-center flex-wrap">
				<input type="hidden" name="type" value="student">
					<div class="d-flex flex-wrap gap-3 p-1">
						<p class="mb-0 me-5 align-self-center">学生情報</p>
						<%-- 学生番号 --%>
						<div class="d-flex flex-column" style="width: 260px;">
							<label class="form-label">学生番号</label>
							<input class="form-control" type="text" placeholder="学生番号を入力してください" required maxlength="10">
						</div>

						<%-- 検索ボタン --%>
						<div class="align-self-center ms-4">
							<button type="submit" class="btn btn-secondary">検索</button>
						</div>
					</div>
				</form>
			</div>
			<c:if test="${empty type}">
				<p class="text-info">科目情報を選択または学生情報を入力して検索ボタンをクリックしてください。</p>
			</c:if>

			<%-- 検索エラー画面 --%>
			<c:if test="${not empty error && type =='subject'}">
				<div class="alert alert-warning" role="alert">
					${error}
				</div>
			</c:if>

			<%-- 科目別成績の検索結果画面 --%>
			<%-- subjectNameのチェック --%>
			<c:if test="${not empty testList && not empty subjectName && type =='subject'}">
				<div class="mb-3">
					<label class="form-label">科目：${subjectName}</label>
				</div>
			</c:if>

			<%-- 成績情報の有無をチェック --%>
			<c:if test="${empty testsList && type == 'subject'}">
				<p class="text-body">学生情報が存在しませんでした。</p>
			</c:if>

			<c:if test="${not empty testsList && type =='subject'}">
				<input type="hidden" name="f1" value="${f1}">
				<input type="hidden" name="f2" value="${f2}">
				<input type="hidden" name="f3" value="${f3}">

				<table class="table table table-hover">
					<thead>
						<tr>
							<th>入学年度</th>
							<th>クラス</th>
							<th>学生番号</th>
							<th>氏名</th>
							<th>1回</th>
							<th>2回</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="pair" items="${testsList}">
							<c:set var="test1" value="${pair[0]}" />
							<c:set var="test2" value="${pair[1]}" />
							<tr>
								<td>${test1.student.entYear}</td>
								<td>${test1.student.classNum}</td>
								<td>${test1.student.no}</td>
								<td>${test1.student.name}</td>
								<%-- 1回目 --%>
								<td><c:choose>
								  <c:when test="${not empty test1}">${test1.point}</c:when>
								  <c:otherwise>-</c:otherwise>
								</c:choose></td>
								<%-- 2回目 --%>
								<td><c:choose>
								  <c:when test="${not empty test2}">${test2.point}</c:when>
								  <c:otherwise>-</c:otherwise>
								</c:choose></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>

			<%-- 学生別成績の結果画面 --%>
			<%-- subjectNameをチェック --%>
			<c:if test="${not empty subjectName && type =='student'}">
				<div class="mb-3">
					<label class="form-label">氏名：${student.name}</label>
				</div>
			</c:if>

			<c:if test="${not empty tests && type =='student'}">
				<input type="hidden" name="f1" value="${f1}">
				<input type="hidden" name="f2" value="${f2}">
				<input type="hidden" name="f3" value="${f3}">

				<table class="table table table-hover">
					<thead>
						<tr>
							<th>科目名</th>
							<th>科目コード</th>
							<th>回数</th>
							<th>点数</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="test" items="${tests}">
							<tr>
								<td>${test.subject.name}</td>
								<td>${test.subject.cd}</td>
								<td>${test.student.no}</td>
								<td>${test.point}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>

		</section>
	</c:param>
</c:import>