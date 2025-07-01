<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
  <c:param name="title">科目情報削除</c:param>

  <c:param name="scripts"></c:param>

  <c:param name="content">
    <section class="me-4">
      <!-- ① 科目情報削除 -->
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報削除</h2>

      <div class="border p-4 mb-3 rounded">
        <!-- ② 確認メッセージ -->
        <p>「${subject.name}${subject.code}」を削除してもよろしいですか</p>

        <!-- ③ 削除ボタン -->
        <form action="SubjectDeleteExecute.action" method="post" class="d-inline">
          <input type="hidden" name="cd" value="${subject.code}">
          <button type="submit" class="btn btn-danger">削除</button><br><br><br><br>
        </form>

        <!-- ④ 戻るリンク -->
        <a href="SubjectList.action" class="ms-3">戻る</a>
      </div>
    </section>
  </c:param>
</c:import>
