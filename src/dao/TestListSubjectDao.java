package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bean.School;
import bean.Subject;
import bean.TestListSubject;

public class TestListSubjectDao extends Dao {

	/**
	 * postFilterメソッド 実行結果(ResultSet)をTestListSubject型のリストに変換するメソッド
	 *                    学生毎に複数の成績を持つためpoints(Map)に保存する
	 * @param resultSet:ResultSet
	 *            実行結果
 	 * @return 科目別成績リスト
	 * @throws Exception
	 */
	public List<TestListSubject> postFilter(ResultSet rSet) throws Exception {
		// 学生毎にインスタンスを持ち、学生番号で管理するMap
		Map<String, TestListSubject> map = new LinkedHashMap<>();

		// 科目別成績リストのインスタンスを取得
		List<TestListSubject> list = new ArrayList<>();

		// 実行結果を1件ずつ処理
		while (rSet.next()){
			// 学生番号を取得
			String stuNo = rSet.getString("student_no");

			// 同じ学生の試験データがMapにあるか確認
			TestListSubject tls = map.get(stuNo);

			if (tls == null) {
				// 存在しない場合：新規作成
				tls = new TestListSubject();

				// 基本情報をセット
				tls.setEntYear(rSet.getInt("ent_year"));
				tls.setStudentNo(stuNo);
				tls.setStudentName(rSet.getString("student_name"));
				tls.setClassNum(rSet.getString("class_num"));

				// 点数管理用のMapを初期化
				tls.setPoints(new LinkedHashMap<>());

				//学生番号を主キーにしてマップに追加
				map.put(stuNo, tls);
			}

			// 回数と得点を取得
			int no = rSet.getInt("no");
			int point = rSet.getInt("point");

			// Mapに回数と得点を追加
			if (rSet.wasNull()) {
				// 得点がnullの場合：Mapにnullを入れる
				tls.getPoints().put(no,null);
			} else {
				tls.getPoints().put(no,point);
			}

		}

		// Mapの値をlistにして変換して渡す
		return new ArrayList<>(map.values());
	}
	/**
	 * filterメソッド 学生に紐づいたテスト情報を絞り込む
	 *
	 * @param school:School
	 *            学生
 	 * @return 学生毎の科目別成績
	 * @throws Exception
	 */
	public List<TestListSubject> filter(int entYear, String classNum, Subject subject, School school) throws Exception {
		// 科目別成績リストのインスタンスを取得
		List<TestListSubject> list = new ArrayList<>();
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// プリペアードステートメントにSQL文をセット
			st = con.prepareStatement( "SELECT s.ent_year, s.class_num, s.student_no, s.student_name, t.no, t.point "
		              + "FROM test t "
		              + "INNER JOIN student s ON t.student_no = s.student_no AND t.school_cd = s.school_cd "
		              + "WHERE s.ent_year = ? "
		              + "AND s.class_num = ? "
		              + "AND t.subject_cd = ? "
		              + "AND t.school_cd = ? "
		              + "ORDER BY s.student_no, t.no"
		            );

			// プリペアードステートメントに各条件をバインド
			st.setInt(1, entYear);
			st.setString(2, classNum);
			st.setString(3, subject.getCd());
			st.setString(4, school.getCd());


			// プリペアードステートメントを実行し、結果をセット
			try(ResultSet rs = st.executeQuery()) {;
				// postFilterメソッドを利用し実行結果をテスト情報リストに変換
				list = postFilter(rs);
			}

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

		return list;
	}

}


