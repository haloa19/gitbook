package com.douzone.gitbook.vo;

public class CommentVo {
	private Long no; 
	private String contents;
	private String regDate; 
	private Long userNo;
	private Long timelineNo;
	
	
	private String userId;
	private String userProfile;
	private String userNickname;
	private String userStatus;
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public Long getUserNo() {
		return userNo;
	}
	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}
	public Long getTimelineNo() {
		return timelineNo;
	}
	public void setTimelineNo(Long timelineNo) {
		this.timelineNo = timelineNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}
	public String getUesrNickname() {
		return userNickname;
	}
	public void setUesrNickname(String uesrNickname) {
		this.userNickname = uesrNickname;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	@Override
	public String toString() {
		return "commentVo [no=" + no + ", contents=" + contents + ", regDate=" + regDate + ", userNo=" + userNo
				+ ", timelineNo=" + timelineNo + ", userId=" + userId + ", userProfile=" + userProfile
				+ ", uesrNickname=" + userNickname + "]";
	}
	
	
	
	
}
