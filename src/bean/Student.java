package bean;

import java.io.Serializable;

public class Student extends User implements Serializable {
	/**
	 * 学生番号:String
	 */
	private String no;

	/**
	 * 学生名:String
	 */
	private String name;

	/**
	 * 入学年度:int
	 */
	private int entYear;

	/**
	 * クラス番号:String
	 */
	private String classNum;

	/**
	 * 在学中フラグ:boolean
	 */
	private boolean isAttend;

	/**
	 * 学校コード:School
	 */
	private School school;

	/**
	 * ゲッター・セッター
	 */
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEntYear() {
		return entYear;
	}

	public void setEntYear(int entYear) {
		this.entYear = entYear;
	}

	public String getClassNum() {
		return classNum;
	}

	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}
	public boolean isAttend() {
		return isAttend;
	}

	public void setAttend(boolean isAttend) {
		this.isAttend = isAttend;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}
}
