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
import dao.ClassNumDao;
import dao.Dao;
import dao.SubjectDao;
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
		School school = teacher.getSchool();

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


		//JSPへフォワード 7
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}
