package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Teacher;
import tool.Action;

public class TestListAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// セッションデータからログイン中のユーザーデータを取得
		Teacher teacher = (Teacher) req.getSession().getAttribute("user");

		// ログインしていない場合はログイン画面へリダイレクト
		if (teacher == null || !teacher.isAuthenticated()) {
			res.sendRedirect("../login.jsp");
			return;
		}

		// プルダウンに選択肢を表示するためにloadDropdownOptionsを呼び出す
		loadDropdownOptions(req, teacher.getSchool());

		//JSPへフォワード 7
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}
