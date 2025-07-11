package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

/**
 * 削除確認画面表示用アクション
 */
public class SubjectDeleteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // ログインチェック
        Teacher teacher = (Teacher) req.getSession().getAttribute("user");
        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../login.jsp");
            return;
        }
        School school = teacher.getSchool();

        // パラメータ（削除対象の科目コード）取得
        String cd = req.getParameter("cd");
        if (cd == null || cd.isEmpty()) {
            // 不正アクセス時は一覧画面へリダイレクト
            res.sendRedirect("subject_list.jsp");
            return;
        }

        // 対象科目を取得
        SubjectDao dao = new SubjectDao();
        Subject subject = dao.get(cd, school);
        if (subject == null) {
            // 存在しない場合は一覧に戻す
            res.sendRedirect("subject_list.jsp");
            return;
        }

        // 画面表示用にセット
        req.setAttribute("subject_cd",   subject.getCd());
        req.setAttribute("subject_name", subject.getName());

        // 確認画面へフォワード
        req.getRequestDispatcher("subject_delete.jsp").forward(req, res);
    }
}
