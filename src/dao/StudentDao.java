package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;

public class StudentDao extends Dao {
	/**
	 * getメソッド 学生番号を指定して学生インスタンスを1件取得する
	 *
	 * @param no:String
	 *            学生番号
	 * @return 学生クラスのインスタンス 存在しない場合はnull
	 * @throws Exception
	 */
	public Student get(String no) throws Exception {
		// 学生インスタンスを初期化
		Student stu = new Student();
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// プリペアードステートメントにSQL文をセット
			st = con.prepareStatement("SELECT * FROM student WHERE no=?");
			// プリペアードステートメントに学生番号をバインド
			st.setString(1, no);
			// プリペアードステートメントを実行
			ResultSet rs = st.executeQuery();

			// 学校Daoを初期化
			SchoolDao schoolDao = new SchoolDao();

			if (rs.next()) {
				// リザルトセットが存在する場合
				// 学生インスタンスに検索結果をセット
				stu.setNo(rs.getString("no"));
				stu.setName(rs.getString("name"));
				stu.setEntYear(rs.getInt("ent_year"));
				stu.setClassNum(rs.getString("class_num"));
				stu.setAttend(rs.getBoolean("is_attend"));
				// 学校フィールドには学校コードで検索した学校インスタンスをセット
				stu.setSchool(schoolDao.get(rs.getString("school_cd")));
			} else {
				// リザルトセットが存在しない場合
				// 学生インスタンスにnullをセット
				stu = null;
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

		return stu;
	}

	/**
	 * postFilterメソッド 実行結果(ResultSet)を学生情報リスト(List<Student>)に変換するメソッド
	 *
	 * @param resultSet:ResultSet
	 *            実行結果
	 * @param school:School
	 *            所属学校
 	 * @return 学生情報リスト
	 * @throws Exception
	 */
	public List<Student> postFilter(ResultSet resultSet, School school) throws Exception {
		// 学生情報リストのインスタンスを取得
		List<Student> list = new ArrayList<>();

		// 実行結果を1件ずつ処理
		while (resultSet.next()){
			// 学生インスタンスを取得
			Student stu = new Student();

			// 各情報を学生インスタンスにセット
			stu.setNo(resultSet.getString("no"));
			stu.setName(resultSet.getString("name"));
			stu.setEntYear(resultSet.getInt("ent_year"));
			stu.setClassNum(resultSet.getString("class_num"));
			stu.setAttend(resultSet.getBoolean("is_attend"));
			stu.setSchool(school);

			// 学生情報リストに追加
			list.add(stu);
		}

		return list;
	}

	/**
	 * filterメソッド① 所属学校、入学年、クラス番号、在学フラグで絞り込み
	 *
	 * @param school:School
	 *            所属学校
	 * @param entYear:int
	 *            入学年
	 * @param classNum:String
	 *            学生番号
	 * @param isAttend:boolean
	 *            在学フラグ
 	 * @return
	 * @throws Exception
	 */
	public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception {
		// 学生情報リストのインスタンスを取得
		List<Student> list = new ArrayList<>();
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// プリペアードステートメントにSQL文をセット
			st = con.prepareStatement("SELECT * FROM student "
					+ "WHERE school_cd = ? "
					+ "AND ent_year = ? "
					+ "AND class_num = ? "
					+ "AND is_attend = ?");

			// プリペアードステートメントに学校コード、入学年、クラス番号、在学フラグをバインド
			st.setString(1, school.getCd());
			st.setInt(2, entYear);
			st.setString(3, classNum);
			st.setBoolean(4, isAttend);

			// プリペアードステートメントを実行し、結果をセット
			try(ResultSet rs = st.executeQuery()) {;
				// postFilterメソッドを利用し実行結果を学生情報リストに変換
				list = postFilter(rs, school);
			}

		} catch (Exception e) {
			throw e;
		}

		st.close();
		con.close();

		return list;

	}

	/**
	 * filterメソッド② 所属学校、入学年、在学フラグで絞り込み
	 *
	 * @param school:School
	 *            所属学校
	 * @param entYear:int
	 *            入学年
	 * @param isAttend:boolean
	 *            在学フラグ
 	 * @return
	 * @throws Exception
	 */
	public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception {
		// 学生情報リストのインスタンスを取得
		List<Student> list = new ArrayList<>();
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// プリペアードステートメントにSQL文をセット
			st = con.prepareStatement("SELECT * FROM student "
					+ "WHERE school_cd = ? "
					+ "AND ent_year = ? "
					+ "AND is_attend = ?");

			// プリペアードステートメントに学校コード、入学年、在学フラグをバインド
			st.setString(1, school.getCd());
			st.setInt(2, entYear);
			st.setBoolean(3, isAttend);

			// プリペアードステートメントを実行し、結果をセット
			try(ResultSet rs = st.executeQuery()) {;
				// postFilterメソッドを利用し実行結果を学生情報リストに変換
				list = postFilter(rs, school);
			}

		} catch (Exception e) {
			throw e;
		}

		st.close();
		con.close();

		return list;
	}

	/**
	 * filterメソッド③ 所属学校、在学フラグで絞り込み
	 *
	 * @param school:School
	 *            所属学校
	 * @param isAttend:boolean
	 *            在学フラグ
 	 * @return
	 * @throws Exception
	 */
	public List<Student> filter(School school, boolean isAttend) throws Exception {
		// 学生情報リストのインスタンスを取得
		List<Student> list = new ArrayList<>();
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// プリペアードステートメントにSQL文をセット
			st = con.prepareStatement("SELECT * FROM student "
					+ "WHERE school_cd = ? "
					+ "AND is_attend = ?");

			// プリペアードステートメントに学校コード、在学フラグをバインド
			st.setString(1, school.getCd());
			st.setBoolean(2, isAttend);

			// プリペアードステートメントを実行し、結果をセット
			try(ResultSet rs = st.executeQuery()) {;
				// postFilterメソッドを利用し実行結果を学生情報リストに変換
				list = postFilter(rs, school);
			}

		} catch (Exception e) {
			throw e;
		}

		st.close();
		con.close();

		return list;
	}

	/**
	 * saveメソッド 学生情報の保存
	 * →すでに存在している場合は更新(UPDATE)、存在しなければ(INSERT)
	 *
	 * @param student:Student
	 *            学生インスタンス
 	 * @return true:登録・更新が成功、false:登録・更新が失敗、
	 *
	 * @throws Exception
	 */
	public boolean save(Student student) throws Exception {
		// 保存成否フラグ
		boolean result = false;
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// 指定された学生が存在するか確認
			Student exist = get(student.getNo());

			if (exist == null) {
				// 存在しない場合：新規登録(INSERT)
				st = con.prepareStatement(
						"INSERT INTO student (no, name, ent_year, class_num, is_attend, school_cd) "
						+ "VALUES(?, ?, ?, ?, ?, ?)");

				// プリペアードステートメントに各条件をバインド
				st.setString(1, student.getNo());
				st.setString(2, student.getName());
				st.setInt(3, student.getEntYear());
				st.setString(4, student.getClassNum());
				st.setBoolean(5, student.isAttend());
				st.setString(6, student.getSchool().getCd());

			} else {
				// 存在する場合：更新(UPDATE)
				// 所属学校と入学年度は更新しない
				st = con.prepareStatement(
						"UPDATE student SET name = ?, class_num = ?, is_attend = ? "
						+ "WHERE no = ?");

				// プリペアードステートメントに各情報をバインド
				st.setString(1, student.getName());
				st.setString(2, student.getClassNum());
				st.setBoolean(3, student.isAttend());
				st.setString(4, student.getNo());
			}

			// SQL文実行後、更新行数が1以上なら成功
			int rows = st.executeUpdate();
			result = (rows > 0);

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

		return result;
	}
}
