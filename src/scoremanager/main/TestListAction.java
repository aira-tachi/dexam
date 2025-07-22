package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Teacher;
import dao.ClassNumDao;
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

		// ログインユーザーの所属学校を取得
		School school = teacher.getSchool();

		// プルダウンに選択肢を表示するためにクラス一覧を取得
		ClassNumDao classNumDao = new ClassNumDao();
		List<String> classNumList = classNumDao.filter(school);

		// JSPにデータをセット
		req.setAttribute("classNumList", classNumList);

		//JSPへフォワード
		req.getRequestDispatcher("class_list.jsp").forward(req, res);
	}
}
