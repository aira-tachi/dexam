package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.TestListStudent;

public class TestListStudentDao extends Dao {

	/**
	 * postFilterメソッド 実行結果(ResultSet)をTestListStudent型のリストに変換するメソッド
	 *
	 * @param resultSet:ResultSet
	 *            実行結果
 	 * @return
	 * @throws Exception
	 */
	public List<TestListStudent> postFilter(ResultSet rSet) throws Exception {
		// 学生別成績リストのインスタンスを取得
		List<TestListStudent> list = new ArrayList<>();

		// 実行結果を1件ずつ処理
		while (rSet.next()){
			// 学生別成績インスタンスを取得
			TestListStudent tls = new TestListStudent();

			tls.setSubjectName(rSet.getString("subject_name"));
			tls.setSubjectCd(rSet.getString("subject_cd"));
			tls.setNum(rSet.getInt("no"));
			tls.setPoint(rSet.getInt("point"));

			// 学生別成績リストに追加
			list.add(tls);
		}

		return list;
	}

	/**
	 * filterメソッド 学生に紐づいたテスト情報を絞り込む
	 *
	 * @param school:School
	 *            学生
 	 * @return
	 * @throws Exception
	 */
	public List<TestListStudent> filter(Student student) throws Exception {
		// テスト情報リストのインスタンスを取得
		List<TestListStudent> list = new ArrayList<>();
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// プリペアードステートメントにSQL文をセット
			st = con.prepareStatement("SELECT t.subject_cd, sub.name, t.no, t.point  FROM test t "
					+ "INNER JOIN subject sub ON t.student_cd = sub.cd AND t.school_cd = sub.school_cd "
					+ "WHERE t.student_no = ? "
					+ "AND t.school_cd = ?");

			// プリペアードステートメントに学生番号と学校コードをバインド
			st.setString(1, student.getNo());
			st.setString(2, student.getSchool().getCd());


			// プリペアードステートメントを実行し、結果をセット
			try(ResultSet rs = st.executeQuery()) {;
				// postFilterメソッドを利用し実行結果をテスト情報リストに変換
				list = postFilter(rs);
			}

		} catch (Exception e) {
			throw e;
		}

		st.close();
		con.close();

		return list;

	}
}

