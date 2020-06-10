package com.douzone.gitbook.vo;

public class AlarmVo {
	private Long no;
	private Long userNo;
	private String alarmType;
	private String alarmContents;
	private String alarmCheck;
	private String alarmDate;

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmContents() {
		return alarmContents;
	}

	public void setAlarmContents(String alarmContents) {
		this.alarmContents = alarmContents;
	}

	public String getAlarmCheck() {
		return alarmCheck;
	}

	public void setAlarmCheck(String alarmCheck) {
		this.alarmCheck = alarmCheck;
	}

	public String getAlarmDate() {
		return alarmDate;
	}

	public void setAlarmDate(String alarmDate) {
		this.alarmDate = alarmDate;
	}

	@Override
	public String toString() {
		return "AlarmVo [no=" + no + ", userNo=" + userNo + ", alarmType=" + alarmType + ", alarmContents=" + alarmContents + ", alarmCheck=" + alarmCheck + ", alarmDate=" + alarmDate + "]";
	}

}
