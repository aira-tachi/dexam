package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ClassNum;
import bean.School;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassUpdateExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// セッションデータからログイン中のユーザーデータを取得
		Teacher teacher = (Teacher) req.getSession().getAttribute("user");

		// ログインしていない場合はログイン画面へリダイレクト
		if (teacher == null || !teacher.isAuthenticated()) {
			res.sendRedirect("../login.jsp");
			return;
		}

		School school = teacher.getSchool();
		ClassNumDao dao = new ClassNumDao();

		// 更新前後のクラス番号を取得
		String currentClassNum = req.getParameter("currentClassNum");
		String newClassNum = req.getParameter("newClassNum");

		boolean hasError = false;

		//入力チェック
		int checkNum = 0;
		try {
			checkNum = Integer.parseInt(newClassNum.trim());

			// 範囲チェック
			if (checkNum < 1 || checkNum > 999){
				req.setAttribute("error", "クラス番号は1~999の範囲で入力してください");
				hasError = true;
			}
		} catch (NumberFormatException e) {
			req.setAttribute("error", "クラス番号は数字で入力してください");
			hasError = true;
		}

		// 変更前と同じ番号が入力された場合
		if (!hasError && currentClassNum.equals(newClassNum)) {
			req.setAttribute("error", "現在のクラス番号と同じです");
			hasError = true;
		}

		// 重複チェック
		if (!hasError) {
			ClassNum exist = dao.get(String.valueOf(newClassNum), school);

			if (exist != null) {
				req.setAttribute("error", "このクラス番号はすでに登録されています");
				hasError = true;
			}
		}

		if (hasError) {
			req.setAttribute("classNum", currentClassNum);
			req.setAttribute("newClassNum", newClassNum);
			req.getRequestDispatcher("class_update.jsp").forward(req, res);
			return;
		}

		// 更新処理
		ClassNum target = dao.get(currentClassNum, school);
		boolean result = dao.save(target, newClassNum.trim());

		if (result) {
			// 成功したら、更新完了画面へ遷移
			req.getRequestDispatcher("class_update_done.jsp").forward(req, res);
		} else {
			// 失敗したら、更新画面へ遷移
			req.setAttribute("error_update", "更新に失敗しました");
			req.setAttribute("classNum", currentClassNum);
			req.getRequestDispatcher("class_update.jsp").forward(req, res);
			return;
		}
	}
}

