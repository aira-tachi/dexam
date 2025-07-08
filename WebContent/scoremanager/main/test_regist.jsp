<%-- 成績管理一覧JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">
		成績管理一覧
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>
			<div class="border p-3 mb-3 rounded">
				<%-- 検索フォーム画面 --%>
				<form action="TestRegist.action" method="post"class="d-flex align-items-center gap-3">
					<div class="d-flex flex-wrap gap-3 p-1">
						<%-- 入学年度 --%>
						<div class="d-flex flex-column" style="width: 120px;">
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
						<div class="d-flex flex-column" style="width: 120px;">
							<label class="form-label">クラス</label>
							<select name="f2" id="f2" class="form-select">
								<option value="">--------</option>
								<c:forEach var="classNum" items="${classNums}">
									<option value="${classNum}" <c:if test="${classNum == f2}">selected</c:if>>${classNum}</option>
								</c:forEach>
							</select>
						</div>

						<%-- 科目 --%>
						<div class="d-flex flex-column" style="width: 260px;">
							<label class="form-label">科目</label>
							<select name="f3" id="f3" class="form-select">
								<option value="">--------</option>
								<c:forEach var="subject" items="${subjects}">
									<option value="${subject.cd}" <c:if test="${subject.cd == f3}">selected</c:if>>${subject.name}</option>
								</c:forEach>
							</select>
						</div>

						<%-- 回数 --%>
						<div class="d-flex flex-column" style="width: 120px;">
							<label class="form-label">回数</label>
							<select name="f4" id="f4" class="form-select">
								<option value="">--------</option>
								<c:forEach var="count" begin="1" end="2">
									<option value="${count}" <c:if test="${count == f4}">selected</c:if>>${count}</option>
								</c:forEach>
							</select>
						</div>

						<%-- 検索ボタン --%>
						<div class="d-flex align-items-center ms-3">
							<button type="submit" class="btn btn-secondary">検索</button>
						</div>
					</div>
				</form>
	   		</div>

			<%-- エラー画面 --%>
			<c:if test="${not empty error}">
				<div class="alert alert-danger" role="alert">
					${error}
				</div>
			</c:if>

			<%-- 検索結果画面 --%>
			<%-- subjectName の有無だけをチェック --%>
			<c:if test="${not empty subjectName}">
				<div class="mb-3">
					<label class="form-label">科目：${subjectName}（${f4}回）</label>
				</div>
			</c:if>

			<%-- 検索結果がない場合 --%>
			<c:if test="${empty tests and not empty subjectName}">
				<div class="alert alert-info" role="alert">
					成績情報はありません。
				</div>
			</c:if>

			<%-- 検索結果がある場合 --%>
			<c:if test="${not empty tests}">
				<form action="TestRegistExecute.action" method="post">
					<input type="hidden" name="f1" value="${f1}">
					<input type="hidden" name="f2" value="${f2}">
					<input type="hidden" name="f3" value="${f3}">
					<input type="hidden" name="f4" value="${f4}">

					<table class="table table table-hover">
						<thead>
							<tr>
								<th>入学年度</th>
								<th>クラス</th>
								<th>学生番号</th>
								<th>氏名</th>
								<th>点数</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="test" items="${tests}" varStatus="status">
								<tr>
									<td>${test.student.entYear}</td>
									<td>${test.student.classNum}</td>
									<td>
										${test.student.no}
										<input type="hidden" name="studentNo" value="${test.student.no}">
									</td>
									<td>${test.student.name}</td>
									<td>
										<input type="text" name="point" class="form-control" value="${test.point}" min="0" max="100" style="width: 200px;">
										<%-- エラー表示 --%>
										<c:if test="${not empty pointError[status.index]}">
											<div class="text-warning">${pointError[status.index]}</div>
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<div class="d-flex justify-content-start">
						<button type="submit" class="btn btn-secondary">登録して終了</button>
					</div>
				</form>
			</c:if>
		</section>
	</c:param>
</c:import>