<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
    <!-- ページタイトル -->
    <c:param name="title">得点管理システム</c:param>

    <!-- メインコンテンツ -->
    <c:param name="content">
        <div id="wrap_box">
            <!-- 1. 画面タイトル h2 -->
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2">科目情報変更</h2>

            <div id="wrap_box">
                <!-- 完了メッセージ -->
                <p class="text-center" style="background-color:#66CC99; margin-bottom: 120px;">変更が完了しました</p>

                <!-- 戻るリンク -->
                <a href="../SubjectList.action" class="me-5">科目一覧</a>
            </div>
        </div>
    </c:param>
</c:import>
