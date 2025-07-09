package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {
	/**
	 * getメソッド テスト情報のインスタンスを1件取得する
	 *
	 * @param student:Student
	 *            学生情報
	 * @param subject:Subject
	 *            科目情報
	 * @param school:School
	 *            所属学校
	 * @param no:int
	 *            回数
	 * @return テストクラスのインスタンス 存在しない場合はnull
	 * @throws Exception
	 */
	public Test get(Student student, Subject subject, School school, int no) throws Exception {
		// テストインスタンスを初期化
		Test test = new Test();
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// プリペアードステートメントにSQL文をセット
			st = con.prepareStatement("SELECT * FROM test "
					+ "WHERE student_no = ? "
					+ "AND subject_cd = ? "
					+ "AND school_cd = ? "
					+ "AND no = ?");
			// プリペアードステートメントに各条件をバインド
			st.setString(1, student.getNo());
			st.setString(2, subject.getCd());
			st.setString(3, school.getCd());
			st.setInt(4, no);

			// プリペアードステートメントを実行
			ResultSet rs = st.executeQuery();


			if (rs.next()) {
				// リザルトセットが存在する場合
				// テストインスタンスに検索結果をセット
				test.setStudent(student);
				test.setSubject(subject);
				test.setSchool(school);
				test.setNo(no);
				test.setClassNum(rs.getString("class_num"));
				test.setPoint(rs.getInt("point"));
			} else {
				// リザルトセットが存在しない場合
				// 学生インスタンスにnullをセット
				test = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			// プリペアードステートメントを閉じる
			if (st != null) {
				try {
					st.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			// コネクションを閉じる
			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}

		return test;
	}

	/**
	 * postFilterメソッド 実行結果(ResultSet)をテスト情報リスト(List<Test>)に変換するメソッド
	 *
	 * @param resultSet:ResultSet
	 *            実行結果
	 * @param school:School
	 *            所属学校
 	 * @return テスト情報のリスト
	 * @throws Exception
	 */
	public List<Test> postFilter(ResultSet rSet, School school) throws Exception {
		// テスト情報リストのインスタンスを取得
		List<Test> list = new ArrayList<>();

		// 実行結果を1件ずつ処理
		while (rSet.next()){
			// 学生インスタンスを取得
			Test test = new Test();

			// 学生情報の一部をテストインスタンスにセット
			Student stu = new Student();
			stu.setNo(rSet.getString("student_no"));
			stu.setClassNum(rSet.getString("student_class_num"));
			stu.setName(rSet.getString("name"));
			stu.setEntYear(rSet.getInt("ent_year"));
			stu.setSchool(school);
			test.setStudent(stu);


			// 科目情報の一部をテストインスタンスにセット
			Subject subject = new Subject();
			subject.setCd(rSet.getString("subject_cd"));
			subject.setSchool(school);
			test.setSubject(subject);

			// 所属学校をテストインスタンスにセット
			test.setSchool(school);

			// クラス番号をセット
			test.setClassNum(rSet.getString("test_class_num"));

			// 回数をテストインスタンスにセット
			test.setNo(rSet.getInt("no"));

			// 得点がnullでも対応
			int point = rSet.getInt("point");
			if (rSet.wasNull()) {
				test.setPoint(null);
			} else {
				test.setPoint(point);
			}

			// テスト情報リストに追加
			list.add(test);
		}

		return list;
	}

	/**
	 * filterメソッド 入学年、クラス番号、科目、回数、所属学校で絞り込み
	 *                未登録含め、学生のテスト情報を取得(studentとtestを結合)
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
	public List<Test> filter(int entYear, String classNum, Subject subject, int no, School school) throws Exception {
		// テスト情報リストのインスタンスを取得
		List<Test> list = new ArrayList<>();
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// プリペアードステートメントにSQL文をセット
			st = con.prepareStatement(
				    "SELECT s.no AS student_no, s.name, s.ent_year, s.class_num AS student_class_num, " +
				    "t.subject_cd, t.no, t.point, t.class_num AS test_class_num " +
				    "FROM student s " +
				    "LEFT JOIN test t ON s.no = t.student_no AND t.subject_cd = ? AND t.no = ? AND t.school_cd = ? " +
				    "WHERE s.ent_year = ? " +
				    "AND s.class_num = ? " +
				    "AND s.school_cd = ?"
				);

			// プリペアードステートメントに各条件をバインド
			st.setString(1, subject.getCd());
			st.setInt(2, no);
			st.setString(3, school.getCd());
			st.setInt(4, entYear);
			st.setString(5, classNum);
			st.setString(6, school.getCd());

			// プリペアードステートメントを実行し、結果をセット
			try(ResultSet rs = st.executeQuery()) {;
				// postFilterメソッドを利用し実行結果をテスト情報リストに変換
				list = postFilter(rs, school);
			}

		}  finally {
			// プリペアードステートメントを閉じる
			if (st != null) {
				try {
					st.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
					}
			// コネクションを閉じる
			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}

		return list;

	}

	/**
	 * saveメソッド① 複数のテスト情報の保存
	 *
	 * @param list:List<Test>
	 *            テスト情報のリスト
 	 * @return true:登録・更新が成功、false:登録・更新が失敗、
	 *
	 * @throws Exception
	 */
	public boolean save(List<Test> list) throws Exception {
		// 保存成否フラグ(複数県のうち1件でも失敗したらFalseにする)
		boolean result = true;
		// コネクションを確立
		Connection con = getConnection();

		try {
			// forループで1件ずつsaveメソッド②に渡して保存
			for (Test test : list) {
				boolean r = save(test, con);  // 1件ずつ保存
				if (!r) {
					result = false;  // 失敗したらfalse
				}
			}
		} finally {
			// コネクションを閉じる
			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}

		return result;
	}

	/**
	 * saveメソッド② 1件のテスト情報の保存
	 *
	 * @param test:Test
	 *            テストインスタンス
	 * @param connection:Connection
	 *            DB接続
 	 * @return true:登録・更新が成功、false:登録・更新が失敗、
	 *
	 * @throws Exception
	 */
	public boolean save(Test test, Connection con) throws Exception {
		// 保存成否フラグ
		boolean result = false;
		PreparedStatement st = null;

		try {
			// 指定されたテスト情報が存在するか確認
			Test exist = get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());

			if (exist != null) {
				// 存在する場合：更新(UPDATE)
				// 得点(point)だけ更新する
				st = con.prepareStatement(
						"UPDATE test SET point = ? "
						+ "WHERE student_no = ? "
						+ "AND subject_cd = ? "
						+ "AND school_cd = ? "
						+ "AND no = ?");

				// プリペアードステートメントに各条件をバインド

				// 得点がnullならserNullでバインド
				if (test.getPoint() == null) {
					st.setNull(1, java.sql.Types.INTEGER);
				} else {
					st.setInt(1, test.getPoint());
				}

				st.setString(2, test.getStudent().getNo());
				st.setString(3, test.getSubject().getCd());
				st.setString(4, test.getSchool().getCd());
				st.setInt(5, test.getNo());

			} else {
			    	// 存在しない場合：登録(INSERT)
					st = con.prepareStatement(
					        "INSERT INTO test (student_no, subject_cd, school_cd, no, class_num, point) "
					        + "VALUES (?, ?, ?, ?, ?, ?)");

					// プリペアードステートメントに各条件をバインド
				    st.setString(1, test.getStudent().getNo());
				    st.setString(2, test.getSubject().getCd());
				    st.setString(3, test.getSchool().getCd());
				    st.setInt(4, test.getNo());
				    st.setString(5, test.getClassNum());

					// 得点がnullならserNullでバインド
					if (test.getPoint() == null) {
						st.setNull(6, java.sql.Types.INTEGER);
					} else {
						st.setInt(6, test.getPoint());
					}
			}

			// SQL文実行後、更新行数が1以上なら成功
			int rows = st.executeUpdate();
			result = (rows > 0);

		} finally {
			// プリペアードステートメントを閉じる
			if (st != null) {
				try {
					st.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}

		return result;
	}
}


