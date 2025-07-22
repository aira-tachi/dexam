<%-- サイドバー --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<style>
  .nav-pills .nav-item a {
    display: inline-block;
    white-space: nowrap;         /* 改行防止：縦書きになるのを防ぐ */
    writing-mode: horizontal-tb; /* 横書きを明示 */
    font-size: clamp(0.9rem, 1.5vw, 1.2rem); /* 自動で文字サイズ調整 */
  }

  .nav-pills .nav-item {
    font-size: clamp(0.9rem, 1.5vw, 1.2rem); /* 「成績管理」などリンクでない項目も調整 */
  }

  /* スマホ表示時に左右の余白が広すぎるのを調整 */
  @media (max-width: 576px) {
    .nav-pills.px-4 {
      padding-left: 1rem !important;
      padding-right: 1rem !important;
    }
  }
</style>

<ul class="nav nav-pills flex-column mb-auto px-4">
	<li class="nav-item my-3"><a href="Menu.action">メニュー</a></li>
	<li class="nav-item mb-3"><a href="StudentList.action">学生管理</a></li>
	<li class="nav-item">成績管理</li>
	<li class="nav-item mx-3 mb-3"><a href="TestRegist.action">成績登録</a></li>
	<li class="nav-item mx-3 mb-3"><a href="TestList.action">成績参照</a></li>
	<li class="nav-item mb-3"><a href="SubjectList.action">科目管理</a></li>
	<li class="nav-item mb-3"><a href="ClassList.action">クラス管理</a></li>
</ul>
