package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // ログインチェック
        Teacher teacher = (Teacher) req.getSession().getAttribute("user");
        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../login.jsp");
            return;
        }

        // フォームからの入力値を取得
        String no = req.getParameter("no");
        String name = req.getParameter("name");
        String classNum = req.getParameter("class_num");

        // チェックボックスはチェックがある場合だけ値が送信されるためnull判定が必要
        boolean attend = req.getParameter("is_attend") != null;

        // 対象の学生を取得
        StudentDao dao = new StudentDao();
        Student student = dao.get(no);

        if (student == null) {
            req.setAttribute("message", "更新対象の学生が見つかりません。");
            req.getRequestDispatcher("student_list.jsp").forward(req, res);
            return;
        }

        // 学生情報の更新
        student.setName(name);
        student.setClassNum(classNum);
        student.setAttend(attend);

        // 更新処理
        boolean result = dao.save(student);

        // 結果に応じてメッセージをセット
        if (result) {
            req.setAttribute("message", "更新に成功しました。");
        } else {
            req.setAttribute("message", "更新に失敗しました。");
        }

        // 完了画面へ
        req.getRequestDispatcher("student_update_done.jsp").forward(req, res);
    }
}
