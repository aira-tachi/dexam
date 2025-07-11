package tool;

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
import dao.ClassNumDao;
import dao.Dao;
import dao.SubjectDao;

public abstract class Action {

	public abstract void execute(
			HttpServletRequest req, HttpServletResponse res
		) throws Exception;

	// ドロップダウン用データをロードして req にセット
    protected void loadDropdownOptions(HttpServletRequest req, School school)
            throws Exception {
    	ClassNumDao classNumDao = new ClassNumDao();
    	List<String> classNums = classNumDao.filter(school);
    	req.setAttribute("classNums", classNums);

    	SubjectDao subjectDao = new SubjectDao();
		List<Subject> subjects = subjectDao.filter(school);
		req.setAttribute("subjects", subjects);

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
    }
}
