package scoremanager.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentListAction extends Action {

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

        String entYearStr = req.getParameter("ent_year");

        String classNo = req.getParameter("class_num");

        String isAttendStr = req.getParameter("isAttend"); // ← checkbox: "on" or null

        Integer entYear = (entYearStr != null && !entYearStr.isEmpty()) ? Integer.parseInt(entYearStr) : null;

        StudentDao studentDao = new StudentDao();

        ArrayList<Student> studentList = new ArrayList<>();

        if (isAttendStr == null) {

            // 在学チェックなし → 両方取得

            if (entYear != null && classNo != null && !classNo.isEmpty()) {

                studentList.addAll(studentDao.filter(school, entYear, classNo, true));

                studentList.addAll(studentDao.filter(school, entYear, classNo, false));

            } else if (entYear != null) {

                studentList.addAll(studentDao.filter(school, entYear, true));

                studentList.addAll(studentDao.filter(school, entYear, false));

            } else {

                studentList.addAll(studentDao.filter(school, true));

                studentList.addAll(studentDao.filter(school, false));

            }

        } else {

            // 在学チェックあり → 在学のみ

            boolean isAttend = true;

            if (entYear != null && classNo != null && !classNo.isEmpty()) {

                studentList = new ArrayList<>(studentDao.filter(school, entYear, classNo, isAttend));

            } else if (entYear != null) {

                studentList = new ArrayList<>(studentDao.filter(school, entYear, isAttend));

            } else {

                studentList = new ArrayList<>(studentDao.filter(school, isAttend));

            }

        }

        // 入学年度・クラス一覧を取得

        ArrayList<Integer> entYearList = new ArrayList<>();

        ArrayList<String> classList = new ArrayList<>();

        try (Connection con = studentDao.getConnection()) {

            String sql1 = "SELECT DISTINCT ent_year FROM student WHERE school_cd = ? ORDER BY ent_year DESC";

            try (PreparedStatement st = con.prepareStatement(sql1)) {

                st.setString(1, schoolCd);

                ResultSet rs = st.executeQuery();

                while (rs.next()) {

                    entYearList.add(rs.getInt("ent_year"));

                }

            }

            String sql2 = "SELECT DISTINCT class_num FROM student WHERE school_cd = ? ORDER BY class_num";

            try (PreparedStatement st = con.prepareStatement(sql2)) {

                st.setString(1, schoolCd);

                ResultSet rs = st.executeQuery();

                while (rs.next()) {

                    classList.add(rs.getString("class_num"));

                }

            }

        }

        // JSPに渡す

        req.setAttribute("students", studentList);

        req.setAttribute("entYearList", entYearList);

        req.setAttribute("classList", classList);

        req.setAttribute("schoolCd", schoolCd);

        req.setAttribute("entYear", entYearStr);

        req.setAttribute("classNo", classNo);

        req.setAttribute("isAttend", isAttendStr);

        req.getRequestDispatcher("student_list.jsp").forward(req, res);

    }

}

