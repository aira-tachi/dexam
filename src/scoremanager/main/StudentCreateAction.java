package scoremanager.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentCreateAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
    	 // セッションから教員情報を取得
        Teacher teacher = (Teacher) req.getSession().getAttribute("user");
        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../login.jsp");
            return;

        }

        School school = teacher.getSchool();
        String schoolCd = school.getCd();

        StudentDao studentDao = new StudentDao();

        ArrayList<Integer> entYearList = new ArrayList<>();
        ArrayList<String> classNumList = new ArrayList<>();

        try (Connection con = studentDao.getConnection()) {
            // 入学年度リスト取得
            String sql1 = "SELECT DISTINCT ent_year FROM student WHERE school_cd = ? ORDER BY ent_year DESC";
            try (PreparedStatement st = con.prepareStatement(sql1)) {
                st.setString(1, schoolCd);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    entYearList.add(rs.getInt("ent_year"));
                }
            }

            // クラス番号リスト取得
            String sql2 = "SELECT DISTINCT class_num FROM student WHERE school_cd = ? ORDER BY class_num";
            try (PreparedStatement st = con.prepareStatement(sql2)) {
                st.setString(1, schoolCd);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    classNumList.add(rs.getString("class_num"));
                }
            }
        }

        req.setAttribute("entYearList", entYearList);
        req.setAttribute("classNumList", classNumList);

        req.getRequestDispatcher("student_create.jsp").forward(req, res);
    }
}
