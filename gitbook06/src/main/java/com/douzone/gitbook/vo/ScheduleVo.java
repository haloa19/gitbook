package com.douzone.gitbook.vo;

public class ScheduleVo {
	
	private Long no;
	private String checkDate;
	private String type;
	private String scheduleContents;
	private Long userNo;
	private Long groupNo;
	
	
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScheduleContents() {
		return scheduleContents;
	}
	public void setScheduleContents(String scheduleContents) {
		this.scheduleContents = scheduleContents;
	}
	public Long getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}
	public Long getUserNo() {
		return userNo;
	}
	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}
	
	
	@Override
	public String toString() {
		return "ScheduleVo [no=" + no + ", checkDate=" + checkDate + ", scheduleContents=" + scheduleContents
				+ ", groupNo=" + groupNo + ", userNo=" + userNo + "]";
	}
	
	
}
