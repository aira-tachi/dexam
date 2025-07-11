package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateExecuteAction extends Action {

    @Override

    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        // ログインチェック
        Teacher teacher = (Teacher) req.getSession().getAttribute("user");
        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../login.jsp");
            return;
        }
        School school = teacher.getSchool();

        // フォーム入力値を取得
        String cd   = req.getParameter("cd");
        String name = req.getParameter("name");

        // バリデーション：必須チェック
        boolean hasError = false;
        if (name == null || name.trim().isEmpty()) {
            req.setAttribute("errorName", "このフィールドを入力してください");
            hasError = true;
        }

        if (hasError) {
            // エラー時は再度入力画面へ
            req.setAttribute("cd", cd);
            req.setAttribute("name", name);
            req.getRequestDispatcher("subject_update.jsp").forward(req, res);
            return;
        }

        // 更新実行

        SubjectDao subjectDao = new SubjectDao();
        Subject subject = new Subject();
        subject.setCd(cd);
        subject.setName(name.trim());
        subject.setSchool(school);

        boolean success = subjectDao.save(subject);
        if (!success) {
        	 req.setAttribute("errorMessage", "科目情報の保存に失敗しました。");
        	req.getRequestDispatcher("subject_update.jsp").forward(req, res);
        	return;
        }

        // 更新完了画面へフォワード

        req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);

    }

}
