package com.douzone.gitbook.vo;

public class AlarmVo {
	private Long no;
	private Long userNo;
	private String userId;
	private String alarmType;
	private String alarmContents;
	private String alarmCheck;
	private String alarmDate;
	
	private Long groupNo;
	private String repoName;
	
	private Long alarmRefNo;
	
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Long getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}
	
	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public Long getAlarmRefNo() {
		return alarmRefNo;
	}

	public void setAlarmRefNo(Long alarmRefNo) {
		this.alarmRefNo = alarmRefNo;
	}

	@Override
	public String toString() {
		return "AlarmVo [no=" + no + ", userNo=" + userNo + ", userId=" + userId + ", alarmType=" + alarmType
				+ ", alarmContents=" + alarmContents + ", alarmCheck=" + alarmCheck + ", alarmDate=" + alarmDate
				+ ", groupNo=" + groupNo + ", repoName=" + repoName + ", alarmRefNo=" + alarmRefNo + "]";
	}
}
