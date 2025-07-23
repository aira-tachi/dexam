package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ClassNum;
import bean.School;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassUpdateAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// セッションデータからログイン中のユーザーデータを取得
		Teacher teacher = (Teacher) req.getSession().getAttribute("user");

		// ログインしていない場合はログイン画面へリダイレクト
		if (teacher == null || !teacher.isAuthenticated()) {
			res.sendRedirect("../login.jsp");
			return;
		}

		// 変更するクラス番号を取得
		String classNum = req.getParameter("classNum");

		// ログインユーザーの所属学校を取得
		School school = teacher.getSchool();

		// クラス一覧を取得
		ClassNumDao dao = new ClassNumDao();
		ClassNum target = dao.get(classNum, school);

		if (target == null) {
			// クラス番号が見つからない場合、一覧に戻す
			req.setAttribute("error", "指定されたクラス番号が存在しません");
			req.getRequestDispatcher("class_list.jsp").forward(req, res);
			return;
		}

		// JSPに現在のクラス番号をセット
		req.setAttribute("classNum", target.getClass_num());

		//JSPへフォワード 6
		req.getRequestDispatcher("class_update.jsp").forward(req, res);
	}
}

