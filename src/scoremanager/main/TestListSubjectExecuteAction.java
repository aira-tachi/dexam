package scoremanager.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

public class TestListSubjectExecuteAction extends Action {

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

		// 検索結果画面で表示されるための処理
		// クラス一覧を取得
		ClassNumDao classNumDao = new ClassNumDao();
		List<String> classNums = classNumDao.filter(school);
		req.setAttribute("classNums", classNums);

		// 科目一覧を取得
		SubjectDao subjectDao = new SubjectDao();
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


		// 検索条件(入学年度、クラス、科目、回数)を取得
		String f1 = req.getParameter("f1");  // 入学年度
		String f2 = req.getParameter("f2");  // クラス
		String f3 = req.getParameter("f3");  // 科目コード

		// 入学年度、クラス、科目が未選択の場合はエラーメッセージを表示
		if (f1 == null || f2 == null || f3 == null || f1.isEmpty() || f2.isEmpty() || f3.isEmpty()) {
			req.setAttribute("error", "入学年度、クラス、科目を選択してください。");
			req.setAttribute("type", "subject");
			req.getRequestDispatcher("test_list.jsp").forward(req, res);
			return;
		}

		// 入学年を取得して数値化
		int entYear = Integer.parseInt(f1);

		// 科目情報の取得
		Subject subject = subjectDao.get(f3, school);

		// 成績情報の取得（空データも含む）
		TestDao testDao = new TestDao();

		// 回数別のテストデータリスト
		List<Test> tests1 = testDao.filter(entYear, f2, subject, 1, school);
		List<Test> tests2 = testDao.filter(entYear, f2, subject, 2, school);

		// 学生番号をキーにしたテストデータのマップを作成
		Map<String, Test[]> merged  = new LinkedHashMap<>();

		// 1回目のテストデータをマージ
		for (Test test : tests1) {
			merged.put(test.getStudent().getNo(), new Test[] { test, null });
		}

		// 2回目のテストデータをマージ
		for (Test test : tests2) {
			String no = test.getStudent().getNo();
			if (merged.containsKey(no)) {
				merged.get(no)[1] = test;  // 2回目のテストデータをセット
			} else {
				merged.put(no, new Test[] { null, test });  // 1回目がない場合はnullをセット
			}
		}

		// マージされたテストデータをリストに変換
		List<Test[]> testsList = new ArrayList<>(merged.values());
		req.setAttribute("testsList", testsList);

		// リクエストにセット
		req.setAttribute("subjectName", subject.getName());  // 科目名
		req.setAttribute("type", "subject");  // 検索タイプ
		req.setAttribute("f1", f1);  // 入学年度
		req.setAttribute("f2", f2);  // クラス
		req.setAttribute("f3", f3);  // 科目コード

		// JSPへフォワード
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}
