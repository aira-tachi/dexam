package scoremanager.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

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

        ArrayList<String> entYearList = new ArrayList<>();
        ArrayList<String> classNumList = new ArrayList<>();

        // 今年の年を取得
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        // 今年の10年前から10年後までをリストに追加
        for (int year = currentYear - 10; year <= currentYear + 10; year++) {
            entYearList.add(String.valueOf(year));
        }

        try (Connection con = studentDao.getConnection()) {
            // クラス番号リスト取得
            String sql = "SELECT DISTINCT class_num FROM student WHERE school_cd = ? ORDER BY class_num";
            try (PreparedStatement st = con.prepareStatement(sql)) {
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
