<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
  <c:param name="title">学生情報変更</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生情報変更 <!-- ① --></h2>
      <form action="StudentUpdateExecute.action" method="post" class="px-4" style="max-width:600px;">
        <!-- 入学年度（固定表示） ②③ -->
        <div class="mb-3">
          <label class="form-label">入学年度</label><br />
          <span>${student.entYear}</span>
          <input type="hidden" name="ent_year" value="${student.entYear}" />
        </div>

        <!-- 学生番号（固定表示） ④⑤ -->
        <div class="mb-3">
          <label class="form-label">学生番号</label><br />
          <span>${student.no}</span>
          <input type="hidden" name="no" value="${student.no}" />
        </div>

        <!-- 氏名入力 ⑥⑦ -->
        <div class="mb-3">
          <label for="name" class="form-label">氏名</label>
          <input type="text" id="name" name="name" value="${student.name}" class="form-control" maxlength="30" required />
        </div>

        <!-- クラス選択 ⑧⑨ -->
        <div class="mb-3">
          <label for="class_num" class="form-label">クラス</label>
          <select name="class_num" id="class_num" class="form-select" required>
            <c:forEach var="cls" items="${classNumList}">
              <option value="${cls}" <c:if test="${cls == student.classNum}">selected</c:if>>${cls}</option>
            </c:forEach>
          </select>
        </div>

        <!-- 在学中チェックボックス ⑩⑪ -->
        <div class="mb-3 form-check">
          <input type="checkbox" name="is_attend" id="is_attend" class="form-check-input"
            <c:if test="${student.attend}">checked</c:if> />
          <label for="is_attend" class="form-check-label">在学中</label>
        </div>

        <!-- ボタン類 ⑫⑬ -->
        <div class="mb-3">
          <button type="submit" class="btn btn-primary">変更</button>
          <a href="StudentList.action" class="btn btn-secondary ms-2">戻る</a>
        </div>
      </form>
    </section>
  </c:param>
</c:import>