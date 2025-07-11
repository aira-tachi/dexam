package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

/**
 * 科目削除実行用アクション
 */
public class SubjectDeleteExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // ログインチェック
        Teacher teacher = (Teacher) req.getSession().getAttribute("user");
        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../login.jsp");
            return;
        }
        School school = teacher.getSchool();

        // hidden から科目コード／科目名を取得
        String cd   = req.getParameter("subject_cd");
        String name = req.getParameter("subject_name");
        if (cd == null || cd.trim().isEmpty()) {
            res.sendRedirect("SubjectList.action");
            return;
        }

        // 削除実行
        SubjectDao dao = new SubjectDao();
        Subject subject = new Subject();
        subject.setCd(cd);
        subject.setSchool(school);
        dao.delete(subject);

        // 削除完了画面に渡す情報をセット
        req.setAttribute("subject_cd",   cd);
        req.setAttribute("subject_name", name);

        // 削除完了画面へフォワード（パスは配置に合わせて修正）
        req.getRequestDispatcher("/scoremanager/main/subject_delete_done.jsp")
           .forward(req, res);
    }
}
