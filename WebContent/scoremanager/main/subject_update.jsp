<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム- 科目変更
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
          <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
            科目情報変更
          </h2>

          <div class="border p-3 mb-3 rounded">

		<!-- エラーメッセージ -->
		<c:if test="${not empty errorMessage}">
			<div class="alert alert-danger">${errorMessage}</div>
		</c:if>

		<form action="SubjectUpdateExecute.action" method="post" class="px-3">
		<!-- 科目コード（編集不可） -->
		<div class="mb-3">
		<label class="form-label">科目コード</label>
		<p class="form-control-plaintext"><c:out value="${cd}"/></p>
		<input type="hidden" name="cd" value="${cd}" />
		</div>

		<!-- 科目名（編集可） -->
		<div class="row flex-column mb-3">
		<label for="name" class="col-form-label">科目名</label>
		<div>
		<input
	        type="text"
	        id="name"
	        name="name"
	        class="form-control"
	        value="<c:out value='${name}'/>"
	        required
	      />
		</div>
		</div>

		<!-- ボタン -->
		<div class="d-flex flex-column align-items-start">
			<button type="submit" class="btn btn-primary w-auto">変更</button>
			<a href="SubjectList.action" class="mt-2">戻る</a>
		</div>
		</form>
		</div>
		</section>
	</c:param>
</c:import>