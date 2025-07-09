package scoremanager.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.Dao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistExecuteAction extends Action {

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

		// 検索条件(入学年度、クラス、科目、回数)を取得
		String f1 = req.getParameter("f1");  // 入学年度
		String f2 = req.getParameter("f2");  // クラス
		String f3 = req.getParameter("f3");  // 科目コード
		String f4 = req.getParameter("f4");  // 試験回数

		// 入学年、回数を取得して数値化
		int entYear = Integer.parseInt(f1);
		int no = Integer.parseInt(f4);

		// 科目情報の取得
		SubjectDao subjectDao = new SubjectDao();
		Subject subject = subjectDao.get(f3, school);

		// 成績情報の取得（空データも含む）
		TestDao testDao = new TestDao();
		List<Test> tests = testDao.filter(entYear, f2, subject, no, school);

		// フォームから送信された各学生の 学生番号と点数を取得
		String[] points = req.getParameterValues("point");

		// 点数のエラーチェック
		List<String> pointError = new ArrayList<>();
		for (int i = 0; i < tests.size(); i++) {
			pointError.add(""); // 初期化
		}

		// 登録・変更のあったテストインスタンスのみ保存するリスト
		List<Test> saveTests = new ArrayList<>();

		// エラーフラグ
		boolean hasError = false;

		// 各学生の点数をチェック
		for (int i = 0; i < tests.size(); i++) {
			String pointStr = points[i];

			// 空欄のチェック
			if (pointStr == null || pointStr.isEmpty()) {
				// 空欄はスキップし、エラーメッセージを表示させない
				continue;
			} else {
				try {
					// 点数のチェック
					int point = Integer.parseInt(pointStr);
					if (point < 0 || point > 100) {
						pointError.set(i, "0～100の範囲で入力してください");
						hasError = true;
					} else {
						// 学校・クラス番号・科目・回数・点数を設定し、保存リストに追加
						Test test = tests.get(i);
						tests.get(i).setSchool(school);

						if (test.getStudent().getClassNum() != null) {
						    test.setClassNum(test.getStudent().getClassNum());
						} else {
						    test.setClassNum(f2);
						}
						tests.get(i).setSubject(subject);
						tests.get(i).setNo(no);
						tests.get(i).setPoint(point);

						saveTests.add(test);
						}
				} catch (NumberFormatException e) {
					pointError.set(i, "0～100の範囲で入力してください");
					hasError = true;
				}
			}
		}

		// 1件でもエラーがあった場合、入力画面に戻ってエラーメッセージを表示
		if (hasError) {
			// 入力値やエラー情報をリクエストスコープにセット
			req.setAttribute("f1", f1);
			req.setAttribute("f2", f2);
			req.setAttribute("f3", f3);
			req.setAttribute("f4", f4);

			// エラー画面の選択リストに再セット
			// クラス一覧を取得
			ClassNumDao classNumDao = new ClassNumDao();
			List<String> classNums = classNumDao.filter(school);
			req.setAttribute("classNums", classNums);

			// 科目一覧を取得
			List<Subject> subjects = subjectDao.filter(school);
			req.setAttribute("subjects", subjects);

			// studentテーブルから入学年度を取得
			List<Integer> entYears = new ArrayList<>();
			Dao dao = new Dao();
			try (Connection con = dao.getConnection()) {
				PreparedStatement st = con.prepareStatement(
					"SELECT DISTINCT ent_year FROM student "
					+ "WHERE school_cd = ?");
				st.setString(1, school.getCd());
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					entYears.add(rs.getInt("ent_year"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			req.setAttribute("entYears", entYears);

			// 成績情報とエラー情報をリクエストスコープにセット
			req.setAttribute("tests", tests);
			req.setAttribute("pointError", pointError);
			req.setAttribute("subjectName", subject.getName());

			// エラーメッセージが表示される状態で成績入力画面に戻る
			req.getRequestDispatcher("test_regist.jsp").forward(req, res);
			return;
		}

		// エラーがなければ、成績情報をデータベースに保存
		boolean result = testDao.save(saveTests);

		// JSPへフォワード
		req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
	}
}
