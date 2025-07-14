package scoremanager.main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentCreateExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // 文字エンコーディングの設定（必要に応じて）
        req.setCharacterEncoding("UTF-8");

        // セッションのチェック（ログインしているか確認）
        Teacher teacher = (Teacher) req.getSession().getAttribute("user");
        if (teacher == null) {
            // ログインしていない場合はログイン画面へリダイレクト
            res.sendRedirect("../login.jsp");
            return;
        }

        // パラメータ取得
        String no = req.getParameter("no");
        String name = req.getParameter("name");
        String entYearStr = req.getParameter("ent_year");
        String classNum = req.getParameter("class_num");

        List<String> entYearList = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = currentYear - 10; y <= currentYear + 10; y++) {
            entYearList.add(String.valueOf(y));
        }

        List<String> classNumList = new ArrayList<>();
        try (Connection con = new StudentDao().getConnection();
             PreparedStatement st = con.prepareStatement(
                 "SELECT DISTINCT class_num FROM student WHERE school_cd = ? ORDER BY class_num")) {
            st.setString(1, teacher.getSchool().getCd());
            ResultSet rs = st.executeQuery();
            while (rs.next()) classNumList.add(rs.getString("class_num"));
        }

        // 入力値をセット
        req.setAttribute("entYearList", entYearList);
        req.setAttribute("classNumList", classNumList);
        req.setAttribute("no", no);
        req.setAttribute("name", name);
        req.setAttribute("ent_year", entYearStr);
        req.setAttribute("class_num", classNum);

        // 入学年度選択チェック
        if (entYearStr == null || entYearStr.isEmpty()) {
            req.setAttribute("error_entYear", "入学年度を指定してください。");
            req.getRequestDispatcher("student_create.jsp").forward(req, res);
            return;
        }

        int entYear;
        try {
            entYear = Integer.parseInt(entYearStr);

        } catch (NumberFormatException e) {
            throw e;
        }

        // 所属学校をセッションから取得
        School school = teacher.getSchool();

        // DB保存処理
        StudentDao dao = new StudentDao();

        // 学生番号の重複チェック
        if (dao.get(no) != null) {
            // 重複している場合はエラーメッセージを設定
            req.setAttribute("error_no", "学生番号が重複しています。");
            req.getRequestDispatcher("student_create.jsp").forward(req, res);
            return;
        }

     // クラス選択チェック
        if (classNum == null || classNum.isEmpty()) {
            req.setAttribute("error_classNum", "クラスを指定してください。");
            req.getRequestDispatcher("student_create.jsp").forward(req, res);
            return;
        }

        // 学生インスタンス生成
        Student student = new Student();
        student.setNo(no);
        student.setName(name);
        student.setEntYear(entYear);
        student.setClassNum(classNum);
        student.setAttend(true); // 登録時は在学中とする
        student.setSchool(school);

        boolean result = dao.save(student);

        // 登録結果に応じて処理分岐
        if (result) {
            // 成功 → 登録完了画面へ
            req.getRequestDispatcher("student_create_done.jsp")
            .forward(req, res);

        } else {

            // 失敗 → 入力フォーム画面へ戻す
            req.setAttribute("message", "登録に失敗しました。");
            req.setAttribute("no", no);
            req.setAttribute("name", name);
            req.setAttribute("ent_year", entYearStr);
            req.setAttribute("class_num", classNum);
            req.getRequestDispatcher("student_create.jsp").forward(req, res);

        }

    }

}

