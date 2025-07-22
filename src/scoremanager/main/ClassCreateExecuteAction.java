package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ClassNum;
import bean.School;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassCreateExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// セッションデータからログイン中のユーザーデータを取得
		Teacher teacher = (Teacher) req.getSession().getAttribute("user");

		// ログインしていない場合はログイン画面へリダイレクト
		if (teacher == null || !teacher.isAuthenticated()) {
			res.sendRedirect("../login.jsp");
			return;
		}

		String classNumStr = req.getParameter("classNum");
		int classNum = 0;
		boolean hasError =false;

		// 空チェック
		if (classNumStr == null || classNumStr.trim().isEmpty()) {
			req.setAttribute("error_classNum", "クラス番号を入力してください");
			hasError = true;
		} else {
			try {
				classNum = Integer.parseInt(classNumStr.trim());

				// クラス番号の範囲チェック
				if (classNum < 1 || classNum > 999) {
					req.setAttribute("error_classNum", "クラス番号は1～999の範囲で入力してください");
					hasError = true;
				}
			} catch (NumberFormatException e) {
				req.setAttribute("error_classNum", "クラス番号は数字で入力してください");
				hasError = true;
			}
		}

		// ログインユーザーの所属学校を取得
		School school =teacher.getSchool();
		ClassNumDao dao = new ClassNumDao();

		// 重複チェック
		if (!hasError) {
			ClassNum exist = dao.get(String.valueOf(classNum), school);
			if (exist != null) {
				req.setAttribute("error_classNum", "このクラス番号はすでに登録されています");
				hasError = true;
			}
		}

		if (hasError) {
			req.setAttribute("classNum", classNum);
			req.getRequestDispatcher("class_create.jsp").forward(req, res);
			return;
		}

		// 登録処理
		ClassNum newClass = new ClassNum();
		newClass.setClass_num(String.valueOf(classNum));
		newClass.setSchool(school);

		boolean result = dao.save(newClass);

		if (result) {
			//成功なら登録完了画面へフォワード
			req.getRequestDispatcher("class_create_done.jsp").forward(req, res);
		} else {
			//失敗なら登録画面へフォワード
			req.setAttribute("error", "クラスの登録に失敗しました。");
			req.getRequestDispatcher("class_create.jsp").forward(req, res);
		}

	}
}
