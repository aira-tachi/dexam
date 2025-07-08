package scoremanager.main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestListStudentExecuteAction extends Action {

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

		// 検索条件(学生番号)を取得
		String f4 = req.getParameter("f4");

		// 学生情報の取得
		StudentDao studentDao = new StudentDao();
		Student student = studentDao.get(f4);

		// 全科目情報の取得
		SubjectDao subjectDao = new SubjectDao();
		List<Subject> subjects = subjectDao.filter(school);

		// 科目コードをキーにしたテストデータのマップを作成
		Map<String, Test[]> merged  = new LinkedHashMap<>();

		// 各科目ごとに1回目と2回目のテストデータを取得
		TestDao testDao = new TestDao();

		if (student != null) {
			for (Subject subject : subjects) {
				// 1回目のテストデータを取得
				Test tests1 = testDao.get(student, subject, school, 1);

				// 2回目のテストデータを取得
				Test tests2 = testDao.get(student, subject, school, 2);

				// 成績が登録されている場合はテストデータをマージ
				if (tests1 != null && tests2 != null) {
					merged.put(subject.getCd(), new Test[] { tests1, tests2 });
				}
			}
		}

		// マージされたテストデータをリストに変換
		List<Test[]> testsList = new ArrayList<>(merged.values());

		// リクエストにセット
		req.setAttribute("type", "student");       // 検索タイプ
		req.setAttribute("f4", f4);                // 学生番号
		req.setAttribute("student", student);      // 学生情報
		req.setAttribute("testsList", testsList);  // 成績情報

		// JSPへフォワード
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}
