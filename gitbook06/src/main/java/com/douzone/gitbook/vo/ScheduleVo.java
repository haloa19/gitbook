package com.douzone.gitbook.vo;

public class ScheduleVo {

	private Long no;
	private String checkDate;
	private String type;
	private String scheduleContents;
	private Long userNo;
	private Long groupNo;
	private Long scheduleRefNo;

	private String id;

	private Long groupUserNo;
	private String groupUserId;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getGroupUserNo() {
		return groupUserNo;
	}
	
	public Long getScheduleRefNo() {
		return scheduleRefNo;
	}

	public void setScheduleRefNo(Long scheduleRefNo) {
		this.scheduleRefNo = scheduleRefNo;
	}

	public void setGroupUserNo(Long groupUserNo) {
		this.groupUserNo = groupUserNo;
	}
	
	public String getGroupUserId() {
		return groupUserId;
	}

	public void setGroupUserId(String groupUserId) {
		this.groupUserId = groupUserId;
	}

	@Override
	public String toString() {
		return "ScheduleVo [no=" + no + ", checkDate=" + checkDate + ", type=" + type + ", scheduleContents="
				+ scheduleContents + ", userNo=" + userNo + ", groupNo=" + groupNo + ", id=" + id + ", groupUserNo="
				+ groupUserNo + ", groupUserId=" + groupUserId + "]";
	}
}
