package com.douzone.gitbook.vo;

public class GitVo {
	
	private Long no; 
	private Long userNo;
	private Long groupNo;
	private String description;
	private String visible;
	private String gitName;
	private String regDate;
	private String userId;
	
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
	public Long getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String getGitName() {
		return gitName;
	}
	public void setGitName(String gitName) {
		this.gitName = gitName;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "RepositoryVo [no=" + no + ", userNo=" + userNo + ", groupNo=" + groupNo + ", description=" + description
				+ ", visible=" + visible + ", gitName=" + gitName + ", regDate=" + regDate + "]";
	}

	
	

}
