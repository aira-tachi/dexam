package bean;

import java.io.Serializable;

public class Test extends User implements Serializable {
	/**
	 * 学生:Student
	 */
	private Student student;

	/**
	 * クラス番号:String
	 */
	private String classNum;

	/**
	 * 科目:Subject
	 */
	private Subject subject;

	/**
	 * 学校:School
	 */
	private School school;

	/**
	 * 回数:int
	 */
	private int no;

	/**
	 * 得点:int(得点がnullの可能性があるためIntegerに変更)
	 */
	private Integer point;

	/**
	 * ゲッター・セッター
	 */
	public Student getStudent() {
	    return student;
	}

	public void setStudent(Student student) {
	    this.student = student;
	}

	public String getClassNum() {
	    return classNum;
	}

	public void setClassNum(String classNum) {
	    this.classNum = classNum;
	}

	public Subject getSubject() {
	    return subject;
	}

	public void setSubject(Subject subject) {
	    this.subject = subject;
	}

	public School getSchool() {
	    return school;
	}

	public void setSchool(School school) {
	    this.school = school;
	}

	public int getNo() {
	    return no;
	}

	public void setNo(int no) {
	    this.no = no;
	}

	public Integer getPoint() {
	    return point;
	}

	public void setPoint(Integer point) {
	    this.point = point;
	}

}
