<%-- クラス番号変更JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">クラス情報変更</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">クラス情報変更</h2>

			<c:if test="${not empty error_update}">
				<div class="text text-warning">${error_update}</div>
			</c:if>

			<form action="ClassUpdateExecute.action" method="post">
				<!-- 現在のクラス番号 -->
				<div class="mb-3">
					<label>現在のクラス番号</label>
					<p class="form-control-plaintext">${classNum}</p>
					<input type="hidden" name="currentClassNum" value="${classNum}">
				</div>

				<!-- 新しいクラス番号 -->
				<div class="mb-3">
					<label>新しいクラス番号</label>
					<input type="text" id="newClassNum" name="newClassNum" class="form-control"
					 value="${newClassNum}" required>
					<c:if test="${not empty error}">
						<div class="text-warning">${error}</div>
					</c:if>
				</div>

				<!-- ボタン -->
				<div class="mb-3">
					<button type="submit" class="btn btn-primary">変更</button>
					<a href="ClassList.action" class="btn btn-secondary ms-2">戻る</a>
				</div>
			</form>
		</section>
	</c:param>
</c:import>