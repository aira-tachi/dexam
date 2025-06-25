package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;


public class ClassNumDao extends Dao {
	/**
	 * getメソッド クラス番号と学校コードを指定してクラス番号インスタンスを１件取得する
	 *
	 * @param class_num:String
	 *            クラス番号
	 * @param school:School
	 *            クラス番号
 	 * @return 学校クラスのインスタンス 存在しない場合はnull
	 * @throws Exception
	 */
	public ClassNum get(String class_num, School school) throws Exception {
		// クラス番号インスタンスを初期化
		ClassNum cn = new ClassNum();
		// データベースへのコネクションを確率
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// プリペアードステートメントにSQL文をセット
			st = con.prepareStatement("SELECT * FROM class_num "
					+ "WHERE class_num = ? "
					+ "AND school_cd = ?");
			// プリペアードステートメントにクラス番号と学校コードをバインド
			st.setString(1, class_num);
			st.setString(2, school.getCd());
			// プリペアードステートメントを実行
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				// リザルトセットが存在する場合
				// クラス番号インスタンスにクラス番号と学校をセット
				cn.setClass_num(rs.getString("class_num"));
				cn.setSchool(school);

			} else {
				// 存在しない場合
				// クラス番号インスタンスにnullをセット
				cn = null;
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
		return cn;
	}

	/**
	 * filterメソッド 所属学校に紐づいたクラス番号の一覧を取得
	 *
	 * @param school:School
	 *            所属学校
 	 * @return クラス番号のリスト
	 * @throws Exception
	 */
	public List<String> filter(School school) throws Exception {
		// クラスリストのインスタンスを取得
		List<String> list = new ArrayList<>();
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// プリペアードステートメントにSQL文をセット
			st = con.prepareStatement("SELECT * FROM clas_snum "
					+ "WHERE school_cd = ?");

			// プリペアードステートメントに学
			st.setString(1, school.getCd());

			// プリペアードステートメントを実行し、結果をセット
			ResultSet rs = st.executeQuery();

			// 実行結果を1件ずつ処理
			while (rs.next()){
				// クラス番号インスタンスを取得
				 ClassNum cn = new ClassNum();

				// 各情報をクラス番号インスタンスにセット
				cn.setClass_num(rs.getString("class_num"));
				cn.setSchool(school);

				// リストに追加
				list.add(cn.getClass_num());
			}

		} catch (Exception e) {
			throw e;
		}

		st.close();
		con.close();

		return list;
	}

	/**
	 * saveメソッド① クラス番号の新規登録
	 *
	 * @param classNum:ClassNum
	 *            クラス番号インスタンス
 	 * @return true:登録成功、false:登録失敗
	 *
	 * @throws Exception
	 */
	public boolean save(ClassNum classNum) throws Exception {
		// 成否フラグ
		boolean result = false;
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// 指定されたクラス番号が存在するか確認
			ClassNum exist = get(classNum.getClass_num(), classNum.getSchool());

			if (exist == null) {
				// 存在しない場合：新規登録(INSERT)
				st = con.prepareStatement(
						"INSERT INTO class_num (class_num, school_cd) "
						+ "VALUES(?, ?)");

				// プリペアードステートメントに各情報をバインド
				st.setString(1, classNum.getClass_num());
				st.setString(2, classNum.getSchool().getCd());


				// SQL文実行後、更新行数が1以上なら成功
				int rows = st.executeUpdate();
				result = (rows > 0);

			} else {
				// 存在する場合：trueを返して終了
				result = true;
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

		return result;
	}


	/**
	 * saveメソッド② クラス番号の更新
	 *
	 * @param classNum:ClassNum
	 *            現在のクラス番号
	 * @param newClassNum:String
	 *            新しいクラス番号
 	 * @return true登録・更新が成功、false:登録・更新が失敗
	 *
	 * @throws Exception
	 */
	public boolean save(ClassNum classNum, String newClassNum) throws Exception {
		// 保存成否フラグ
		boolean result = false;
		// コネクションを確立
		Connection con = getConnection();
		// プリペアードステートメント
		PreparedStatement st = null;

		try {
			// 存在しない場合：新規登録(INSERT)
			st = con.prepareStatement(
					"UPDATE class_num SET class_num = ? "
					+ "WHERE class_num = ? AND school_cd = ?");

			// プリペアードステートメントに各情報をバインド
			st.setString(1, newClassNum);
			st.setString(2, classNum.getClass_num());
			st.setString(3, classNum.getSchool().getCd());

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



