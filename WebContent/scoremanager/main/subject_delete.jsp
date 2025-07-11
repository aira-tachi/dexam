<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム - 科目情報削除</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <section class="me-4">
            <!-- 1. 画面タイトル -->
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
                科目情報削除
            </h2>

            <div class="border p-3 mb-3 rounded">
                <!-- 3. 削除ボタンを含むフォーム -->
                <form action="SubjectDeleteExecute.action" method="post" class="px-3">
                    <!-- 2. 確認メッセージ -->
                    <p class="mb-4">
                        「${subject_name}（${subject_cd}）を削除してよろしいですか？」
                    </p>

                    <!-- 5・6. 科目コード/科目名(hidden) -->
                    <input type="hidden" name="subject_cd"   value="${subject_cd}"   />
                    <input type="hidden" name="subject_name" value="${subject_name}" />

                    <!-- 3. 削除ボタン -->
                    <button type="submit" class="btn btn-danger">削除</button>
                </form>

                <!-- 4. 戻るリンク -->
                <div class="mt-2">
                    <a href="SubjectList.action" class="text-decoration-none">
                        <br><br><br>戻る
                    </a>
                </div>
            </div>
        </section>
    </c:param>
</c:import>
