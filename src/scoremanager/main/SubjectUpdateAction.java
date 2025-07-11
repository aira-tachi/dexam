package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateAction extends Action {

    @Override

    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        // ログインチェック

        Teacher teacher = (Teacher) req.getSession().getAttribute("user");

        if (teacher == null || !teacher.isAuthenticated()) {

            res.sendRedirect("../login.jsp");

            return;

        }

        School school = teacher.getSchool();

        // リクエストパラメータを取得

        String cd = req.getParameter("cd");

        String name = req.getParameter("name");

        SubjectDao subjectDao = new SubjectDao();

        if (name == null) {

            // ■ 初期表示(GET)

            if (cd == null || cd.isEmpty()) {

                res.sendRedirect("subject_list.jsp");

                return;

            }

            Subject subject = subjectDao.get(cd, school);

            if (subject == null) {

                res.sendRedirect("subject_list.jsp");

                return;

            }

            req.setAttribute("cd", subject.getCd());

            req.setAttribute("name", subject.getName());

            req.getRequestDispatcher("subject_update.jsp").forward(req, res);

        } else {

            // ■ 更新処理(POST) → save()を使う
            if (cd == null || cd.isEmpty() || name.trim().isEmpty()) {
                req.setAttribute("errorMessage", "科目コードおよび科目名は必須です。");
                req.setAttribute("cd", cd);
                req.setAttribute("name", name);
                req.getRequestDispatcher("subject_update.jsp").forward(req, res);
                return;
            }
            Subject subject = subjectDao.get(cd, school);
            if (subject == null) {
                res.sendRedirect("subject_list.jsp");
                return;
            }
            // データ更新
            subject.setName(name.trim());
            subject.setSchool(school);
            boolean success = subjectDao.save(subject);
            if (!success) {
                req.setAttribute("errorMessage", "科目情報の保存に失敗しました。");
                req.setAttribute("cd", cd);
                req.setAttribute("name", name);
                req.getRequestDispatcher("subject_update.jsp").forward(req, res);
                return;
            }
            // 更新後は一覧画面へリダイレクト
            res.sendRedirect("subject_list.jsp");
        }
    }
}