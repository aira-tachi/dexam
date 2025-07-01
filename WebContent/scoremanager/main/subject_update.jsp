<%-- メニューJSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
          <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
            科目情報変更
          </h2>
          <div class="border p-3 mb-3 rounded">
            <form action="SubjectUpdate.action" method="post" class="px-3">
              <!-- 科目コード（編集不可） -->
              <div class="mb-3">
                        <label class="form-label">科目コード</label>
                        <p class="form-control-plaintext">${subject.code}</p>
                        <input type="hidden" name="code" value="${subject.code}" />
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
  			    value="${subject.name}"
   				   required
   					 />
 				 </div>
				</div>
              <!-- 変更ボタン -->
              <div class="d-flex">
                <button type="submit" class="btn btn-primary">変更</button>
              </div>
            </form>
            <!-- 「戻る」をフォーム外に、ボタンの下に配置 -->
            <div class="mt-3">
              <a href="SubjectList.action" class="text-decoration-none" style="margin-left: 20px;">戻る</a>
            </div>
          </div>
        </section>
     </c:param>
</c:import>
