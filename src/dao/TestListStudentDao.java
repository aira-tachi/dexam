package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import bean.Student;
import bean.Test;

public class TestListStudentDao extends Dao {

	/**
	 * postFilterメソッド
	 *
	 * @param resultSet:ResultSet
	 *            実行結果
	 * @param school:School
	 *            所属学校
 	 * @return テスト情報のリスト
	 * @throws Exception
	 */
	public List<TestListStudent> postFilter(ResultSet rSet) throws Exception {
		// テスト情報リストのインスタンスを取得
		List<TestListStudent> list = new ArrayList<>();

		// 実行結果を1件ずつ処理
		while (resultSet.next()){
			// 学生インスタンスを取得
			Test test = new Test();

			// 学生情報の一部をテストインスタンスにセット
			Student stu = new Student();
			stu.setNo(resultSet.getString("student_no"));
			stu.setClassNum(resultSet.getString("class_num"));
			stu.setSchool(school);
			test.setStudent(stu);


			// 科目情報の一部をテストインスタンスにセット
			Subject subject = new Subject();
			subject.setCd(resultSet.getString("subject_cd"));
			subject.setSchool(school);
			test.setSubject(subject);

			// その他の情報をテストインスタンスにセット
			test.setSchool(school);
			test.setNo(resultSet.getInt("no"));
			test.setPoint(resultSet.getInt("point"));

			// テスト情報リストに追加
			list.add(test);
		}

		return list;
	}

	/**
	 * filterメソッド
	 *
	 * @param entYear:int
	 *            入学年(student)
	 * @param classNum:String
	 *            クラス番号(student)
	 * @param subject:Subject
	 *            科目(test)
	 * @param no:int
	 *            回数(test)
	 * @param school:School
	 *            所属学校(student,test)
 	 * @return テスト情報のリスト
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
			st = con.prepareStatement("SELECT * FROM test t "
					+ "INNER JOIN student s ON t.student_no = s.no "
					+ "WHERE s.ent_year = ? "
					+ "AND t.class_num = ? "
					+ "AND t.subject_cd = ? "
					+ "AND t.no = ? "
					+ "AND t.school_cd = ?");

			// プリペアードステートメントに各条件をバインド
			st.setInt(1, entYear);
			st.setString(2, classNum);
			st.setString(3, subject.getCd());
			st.setInt(4,no) ;
			st.setString(5, school.getCd());

			// プリペアードステートメントを実行し、結果をセット
			try(ResultSet rs = st.executeQuery()) {;
				// postFilterメソッドを利用し実行結果をテスト情報リストに変換
				list = postFilter(rs, school);
			}

		} catch (Exception e) {
			throw e;
		}

		st.close();
		con.close();

		return list;

	}
}


