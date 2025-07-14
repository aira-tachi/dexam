<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
    <c:param name="title">学生情報登録</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <div class="container my-5">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生情報登録</h2>
            <form action="StudentCreateExecute.action" method="post" class="mx-auto" style="max-width: 600px;">

                <div class="mb-3">
                    <label for="no" class="form-label">学生番号</label>
                    <input type="text" name="no" id="no" class="form-control"
                           value="${no}" maxlength="10" required
                           placeholder="学生番号を入力してください">
                </div>

                <div class="mb-3">
                    <label for="name" class="form-label">氏名</label>
                    <input type="text" name="name" id="name" class="form-control"
                           value="${name}" maxlength="30" required
                           placeholder="氏名を入力してください">
                </div>

                <div class="mb-3">
                    <label for="entYear" class="form-label">入学年度</label>
                    <select name="entYearList" id="entYearList" class="form-select" required>
                        <c:forEach var="year" items="${entYearList}">
                            <option value="${year}">${year}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="classNum" class="form-label">クラス</label>
                    <select name="classNumList" id="classNumList" class="form-select" required>
                        <c:forEach var="classNum" items="${classNumList}">
                            <option value="${classNum}">${classNum}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="text-center mt-4">
                    <button type="submit" class="btn btn-primary">登録して終了</button>
                    <a href="StudentList.action" class="btn btn-secondary">戻る</a>
                </div>
            </form>
        </div>
    </c:param>
</c:import>
