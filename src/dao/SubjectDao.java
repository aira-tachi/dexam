package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;

/**
 * SubjectDao クラス
 * 科目(subject)テーブルへの基本的な CRUD 操作を提供する DAO。
 */
public class SubjectDao extends Dao {

    /**
     * getメソッド: 学校コードと科目コードを指定して科目インスタンスを1件取得する
     *
     * @param cd      科目コード
     * @param school  学校インスタンス（学校コードを取得するため）
     * @return 見つかった Subject インスタンス、存在しない場合は null
     * @throws Exception DB接続やクエリ実行時の例外
     */
    public Subject get(String cd, School school) throws Exception {
        Subject subject = null; // 結果格納用
        Connection connection = getConnection(); // 親クラス Dao からコネクション取得
        PreparedStatement stmt = null;
        try {
            // SQL文を準備: subject テーブルから特定コードのレコードを取得
            String sql = "SELECT * FROM subject WHERE cd = ? AND school_cd = ?";
            stmt = connection.prepareStatement(sql);
            // パラメータ設定: 1番目に科目コード、2番目に学校コード
            stmt.setString(1, cd);
            stmt.setString(2, school.getCd());

            // クエリ実行
            ResultSet rs = stmt.executeQuery();
            // 結果が存在する場合
            if (rs.next()) {
                subject = new Subject();
                // カラム値を Subject オブジェクトにマッピング
                subject.setCd(rs.getString("cd"));       // 科目コード
                subject.setName(rs.getString("name"));   // 科目名
                subject.setSchool(school);               // 関連する School インスタンス
            }
        } catch (Exception e) {
            // 例外は呼び出し元に伝搬
            throw e;
        } finally {
            // リソースクローズ: PreparedStatement と Connection
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignore) {
                    // クローズ失敗時も無視（ログ出力推奨）
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignore) {
                    // クローズ失敗時も無視
                }
            }
        }
        return subject;
    }

    /**
     * filterメソッド: 学校を指定してその学校の全科目を取得する
     *
     * @param school  学校インスタンス
     * @return 指定学校の Subject リスト（存在しなければ空リスト）
     * @throws Exception DB接続やクエリ実行時の例外
     */
    public List<Subject> filter(School school) throws Exception {
        List<Subject> list = new ArrayList<>();         // 結果リスト初期化
        Connection connection = getConnection();       // DB接続取得
        PreparedStatement stmt = null;
        try {
            // SQL文を準備: 指定学校コードで絞り込み
            String sql = "SELECT * FROM subject WHERE school_cd = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, school.getCd());           // 学校コード設定

            // クエリ実行
            ResultSet rs = stmt.executeQuery();
            // 結果をリストに追加
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setCd(rs.getString("cd"));     // 科目コード取得
                subject.setName(rs.getString("name")); // 科目名取得
                subject.setSchool(school);               // 紐付く学校情報設定
                list.add(subject);
            }
        } catch (Exception e) {
            // 例外を呼び出し元に伝搬
            throw e;
        } finally {
            // リソース解放
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignore) {}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignore) {}
            }
        }
        return list;
    }

    /**
     * saveメソッド: 科目が存在すれば更新、存在しなければ挿入する
     *
     * @param subject  保存対象の Subject インスタンス
     * @return 成功したら true、失敗または例外時は false
     * @throws Exception DB接続やクエリ実行時の例外
     */
    public boolean save(Subject subject) throws Exception {
        Connection connection = getConnection(); // DB接続
        PreparedStatement stmt = null;
        try {
            // 既存レコードの有無をチェック
            Subject exists = get(subject.getCd(), subject.getSchool());
            if (exists == null) {
                // INSERT: 新規科目登録
                String sql = "INSERT INTO subject (cd, name, school_cd) VALUES (?, ?, ?)";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, subject.getCd());           // 科目コード
                stmt.setString(2, subject.getName());         // 科目名
                stmt.setString(3, subject.getSchool().getCd()); // 学校コード
            } else {
                // UPDATE: 既存科目情報更新
                String sql = "UPDATE subject SET name = ? WHERE cd = ? AND school_cd = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, subject.getName());         // 更新後の科目名
                stmt.setString(2, subject.getCd());           // 条件: 科目コード
                stmt.setString(3, subject.getSchool().getCd()); // 条件: 学校コード
            }
            // INSERT/UPDATE 実行
            int count = stmt.executeUpdate();
            // 更新件数が1以上なら成功とみなす
            return count > 0;
        } catch (Exception e) {
            // 例外伝搬
            throw e;
        } finally {
            // リソースクローズ
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException ignore) {}
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException ignore) {}
            }
        }
    }

    /**
     * deleteメソッド: 指定した Subject を削除する
     *
     * @param subject  削除対象の Subject インスタンス
     * @return 削除成功なら true
     * @throws Exception DB接続やクエリ実行時の例外
     */
    public boolean delete(Subject subject) throws Exception {
        Connection connection = getConnection(); // DB接続取得
        PreparedStatement stmt = null;
        try {
            // DELETE 文を準備: 科目コードと学校コードで削除
            String sql = "DELETE FROM subject WHERE cd = ? AND school_cd = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, subject.getCd());            // 条件: 科目コード
            stmt.setString(2, subject.getSchool().getCd()); // 条件: 学校コード
            // 実行
            int count = stmt.executeUpdate();
            return count > 0; // 削除件数が1以上なら true
        } catch (Exception e) {
            // 例外伝搬
            throw e;
        } finally {
            // リソース解放
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException ignore) {}
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException ignore) {}
            }
        }
    }

    // ←— ここから追加 ——————————————————————————————————

    /**
     * updateメソッド: 科目データを更新する
     *
     * @param subject 更新対象の Subject（cd, name, school がセット済み）
     * @param school  ログイン中ユーザーの所属 School
     * @return 更新件数
     * @throws Exception DB接続やクエリ実行時の例外
     */
    public int update(Subject subject, School school) throws Exception {
        String sql =
            "UPDATE subject "
          + "SET name = ? "
          + "WHERE cd = ? AND school_cd = ?";
        try (
            Connection con = super.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, subject.getName());
            ps.setString(2, subject.getCd());
            ps.setString(3, school.getCd());
            return ps.executeUpdate();
        }
    }

    // ←— ここまで追加 ——————————————————————————————————
}
