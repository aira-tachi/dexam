package scoremanager.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Teacher teacher = (Teacher) req.getSession().getAttribute("user");
        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../login.jsp");
            return;
        }

        String no = req.getParameter("studentNo");
        StudentDao dao = new StudentDao();
        Student student = dao.get(no);
        if (student == null) {
            req.setAttribute("message", "指定された学生は存在しません。");
            req.getRequestDispatcher("student_list.jsp").forward(req, res);
            return;
        }

        // 入学年度リスト（固定範囲で作成）
        ArrayList<String> entYearList = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = currentYear - 10; y <= currentYear + 10; y++) {
            entYearList.add(String.valueOf(y));
        }

        // 既存DAOのfilterメソッドで在学学生全員を取得し、クラス番号のユニークリストを作成
        List<Student> allStudents = dao.filter(student.getSchool(), true);
        Set<String> classNumSet = new HashSet<>();
        for (Student s : allStudents) {
            String clsNum = s.getClassNum();
            if (clsNum != null && !clsNum.isEmpty()) {
                classNumSet.add(clsNum);
            }
        }
        ArrayList<String> classNumList = new ArrayList<>(classNumSet);
        Collections.sort(classNumList);

        // 画面にセット
        req.setAttribute("student", student);
        req.setAttribute("entYearList", entYearList);
        req.setAttribute("classNumList", classNumList);

        req.getRequestDispatcher("student_update.jsp").forward(req, res);
    }
}
