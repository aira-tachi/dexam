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
		<%-- 画面状況によってタイトルが変化 --%>
			<c:choose>
				<%-- 科目情報で検索 --%>
				<c:when test="${type =='subject'}">
					<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績一覧（科目）</h2>
				</c:when>
				<%-- 学生情報で検索 --%>
				<c:when test="${type =='student'}">
					<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績一覧（学生）</h2>
				</c:when>
				<%-- 初回アクセス時 --%>
				<c:otherwise>
					<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>
				</c:otherwise>
			</c:choose>

			<div class="border p-3 mb-3 rounded mx-auto" style="max-width: 780px;">

			<%-- 科目別検索フォーム --%>
				<form action="TestListSubjectExecute.action" method="post" class="d-flex align-items-center flex-wrap">
				 	<input type="hidden" name="type" value="subject">
					<div class="d-flex flex-wrap gap-3 p-1">
						<p class="mb-0 me-4 align-self-center">科目情報</p>
						<%-- 入学年度 --%>
						<div class="d-flex flex-column me-2" style="width: 110px;">
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
						<div class="d-flex flex-column me-2" style="width: 110px;">
							<label class="form-label">クラス</label>
							<select name="f2" id="f2" class="form-select">
								<option value="">--------</option>
								<c:forEach var="classNum" items="${classNums}">
									<option value="${classNum}" <c:if test="${classNum == f2}">selected</c:if>>${classNum}</option>
								</c:forEach>
							</select>
						</div>

						<%-- 科目 --%>
						<div class="d-flex flex-column me-2" style="width: 220px; ">
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

				<%-- 科目検索エラー画面 --%>
					<c:if test="${not empty error && type =='subject'}">
						<p class="text-warning" role="alert">
							${error}
						</p>
					</c:if>

				</form>
				<hr class="my-3">

			<%-- 学生別検索フォーム --%>
				<form action="TestListStudentExecute.action" method="post" class="d-flex align-items-center flex-wrap">
				<input type="hidden" name="type" value="student">
					<div class="d-flex flex-wrap gap-3 p-1">
						<p class="mb-0 me-4 align-self-center">学生情報</p>
						<%-- 学生番号 --%>
						<div class="d-flex flex-column" style="width: 260px;">
							<label class="form-label">学生番号</label>
							<input class="form-control" type="text" name="f4" value="${f4}" placeholder="学生番号を入力してください" required maxlength="10">
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





		<%-- 科目別成績の検索結果画面 --%>
			<%-- subjectNameのチェック --%>
			<c:if test="${not empty testsList && not empty subjectName && type =='subject'}">
				<div class="mb-3">
					<label class="form-label">科目：${subjectName}</label>
				</div>
			</c:if>

			<%-- 学生情報の有無をチェック --%>
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
								<td>
									<c:choose>
										<c:when test="${not empty test1 && not empty test1.point}">
											${test1.point}
										</c:when>
										<c:otherwise>
											-
										</c:otherwise>
									</c:choose>
								</td>
								<%-- 2回目 --%>
								<td>
									<c:choose>
										<c:when test="${not empty test2 && not empty test2.point}">
											${test2.point}
										</c:when>
										<c:otherwise>
											-
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>

		<%-- 学生別成績の結果画面 --%>
			<%-- 学生情報そのものがない（番号未入力 or 不正） --%>
			<c:if test="${empty student && type == 'student'}">
				<p class="text">該当する学生が存在しません。</p>
			</c:if>

			<c:if test="${not empty student && type =='student'}">
				<%-- 氏名・学生番号を表示 --%>
				<div class="mb-3">
					<label class="form-label">氏名：${student.name}(${student.no})</label>
				</div>

				<%-- 学生情報そのものがない（番号未入力 or 不正） --%>
				<c:if test="${empty student && type == 'student'}">
					<p class="text-warning">該当する学生が存在しません。</p>
				</c:if>

				<%-- 成績情報がない場合 --%>
				<c:if test="${empty testsList}">
					<p class="text-body">成績情報が存在しませんでした。</p>
				</c:if>

				<%-- 成績情報がある場合 --%>
				<c:if test="${not empty testsList}">
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
							<c:forEach var="pair" items="${testsList}">
							  <c:set var="test1" value="${pair[0]}" />
							  <c:set var="test2" value="${pair[1]}" />

							  <tr>
							    <td>${test1.subject.name}</td>
							    <td>${test1.subject.cd}</td>
							    <td>1</td>
							    <td>
							      <c:choose>
							        <c:when test="${not empty test1.point}">
							          ${test1.point}
							        </c:when>
							        <c:otherwise>-</c:otherwise>
							      </c:choose>
							    </td>
							  </tr>

							  <tr>
							    <td>${test2.subject.name}</td>
							    <td>${test2.subject.cd}</td>
							    <td>2</td>
							    <td>
							      <c:choose>
							        <c:when test="${not empty test2.point}">
							          ${test2.point}
							        </c:when>
							        <c:otherwise>-</c:otherwise>
							      </c:choose>
							    </td>
							  </tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
			</c:if>
		</section>
	</c:param>
</c:import>