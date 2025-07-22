<%-- クラス登録JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">クラス登録</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">クラス登録</h2>

			<c:if test="${not empty error}">
				<div class="alert-danger">${error}</div>
			</c:if>

			<form action="ClassCreateExecute.action" method="post">
				<label class="form-label">クラス番号</label>
				<input type="text" name="classNum" class="form-control"  maxlength="3" required
					value="${param.classNum != null ? param.classNum : classNum}"
					placeholder="クラス番号を入力してください">

				<c:if test="${not empty error_classNum}">
					<div class="text-danger mt-2">${error_classNum}</div>
				</c:if>

				<div class="text-center mt-4">
					<button type="submit" class="btn btn-primary">登録して終了</button>
					<a href="ClassList.action" class="btn btn-secondary">戻る</a>
				</div>
			</form>
		</section>
	</c:param>
</c:import>