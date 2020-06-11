package com.douzone.gitbook.vo;

public class ChattingMsgVo {
	private Long no;
	private Long userNo;
	
	private String userNickname;
	private String userId;
	private String image;
	
	private Long chattingNo;
	private String sendDate; 
	private String contents; 
	private String readMsg;
	
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
	public String getUserNickname() {
		return userNickname;
	}
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Long getChattingNo() {
		return chattingNo;
	}
	public void setChattingNo(Long chattingNo) {
		this.chattingNo = chattingNo;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getReadMsg() {
		return readMsg;
	}
	public void setReadMsg(String readMsg) {
		this.readMsg = readMsg;
	}
	@Override
	public String toString() {
		return "ChattingMsgVo [no=" + no + ", userNo=" + userNo + ", userNickname=" + userNickname + ", userId="
				+ userId + ", image=" + image + ", chattingNo=" + chattingNo + ", sendDate=" + sendDate + ", contents="
				+ contents + ", readMsg=" + readMsg + "]";
	}
	
	
	
	
	
	
	
	
}
