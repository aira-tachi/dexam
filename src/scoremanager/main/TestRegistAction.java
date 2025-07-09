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
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.Dao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistAction extends Action {

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

		// 選択リスト用のデータ
		// クラス一覧を取得
		ClassNumDao classNumDao = new ClassNumDao();
		List<String> classNums = classNumDao.filter(school);
		req.setAttribute("classNums", classNums);

		// 科目一覧を取得
		SubjectDao subjectDao = new SubjectDao();
		List<Subject> subjects = subjectDao.filter(school);
		req.setAttribute("subjects", subjects);

		//　studentテーブルから入学年度を取得
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

		// 検索条件(入学年度、クラス、科目、回数)を取得
		String f1 = req.getParameter("f1");  // 入学年度
		String f2 = req.getParameter("f2");  // クラス
		String f3 = req.getParameter("f3");  // 科目コード
		String f4 = req.getParameter("f4");  // 試験回数

		// 検索結果画面で表示できるよう、JSP側で選択状態を保持させる
		req.setAttribute("f1", f1);
		req.setAttribute("f2", f2);
		req.setAttribute("f3", f3);
		req.setAttribute("f4", f4);

		// 検索ボタンが押されたかどうかを判定(初回アクセス時はすべてnull)
		boolean isSearchRequested =
			req.getParameter("f1") != null ||
			req.getParameter("f2") != null ||
			req.getParameter("f3") != null ||
			req.getParameter("f4") != null;

		// 検索欄がすべて入力されていたら検索
		// 初回アクセスではエラーメッセージが表示されないようにする
		if (isSearchRequested) {

			if (f1 != null && !f1.isEmpty()
				&& f2 != null && !f2.isEmpty()
				&& f3 != null && !f3.isEmpty()
				&& f4 != null && !f4.isEmpty()) {

				try {
					// 入力値を数値に変換
					int entYear = Integer.parseInt(f1);
					int no = Integer.parseInt(f4);

					// 科目情報の取得
					Subject subject = subjectDao.get(f3, school);

					// 成績情報の取得
					TestDao testDao = new TestDao();
					List<Test> tests = testDao.filter(entYear, f2, subject, no, school);

					// 選択された科目名をセット
					req.setAttribute("subjectName", subject.getName());

					// 検索結果をリクエストにセット
					req.setAttribute("tests", tests);

				} catch (NumberFormatException e) {
					throw e;
				}
			} else {
				// 入力されていない場合は、エラーメッセージ
				req.setAttribute("error", "入学年度、クラス、科目、テストを選択してください。");
				// 検索結果は空のリストをセット
				req.setAttribute("tests", new ArrayList<Student>());
			}
		}
		//JSPへフォワード 6
		req.getRequestDispatcher("test_regist.jsp").forward(req, res);
	}
}

