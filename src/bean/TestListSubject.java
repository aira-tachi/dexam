package bean;

import java.io.Serializable;
import java.util.Map;    // ← 追加

public class TestListSubject extends User implements Serializable {
    /**
     * 入学年:int
     */
    private int entYear;

    /**
     * 学生番号:String
     */
    private String studentNo;

    /**
     * 生徒名:String
     */
    private String studentName;

    /**
     * クラス番号:String
     */
    private String classNum;

    /**
     * 点数:Map
     */
    private Map<Integer,Integer> points;

    /**
     * 単一ポイント
     */
    private int point;    // ← 追加

    /**
     * ゲッター・セッター
     */
    public int getEntYear() {
        return entYear;
    }

    public void setEntYear(int entYear) {
        this.entYear = entYear;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public Map<Integer,Integer> getPoints() {
        return points;
    }

    public void setPoints(Map<Integer,Integer> points) {
        this.points = points;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
