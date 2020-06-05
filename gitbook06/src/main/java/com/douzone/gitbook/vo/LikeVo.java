package com.douzone.gitbook.vo;

public class LikeVo {
	private Long userNo;
	private Long timelineNo;
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
	@Override
	public String toString() {
		return "LikeVo [userNo=" + userNo + ", timelineNo=" + timelineNo + "]";
	}
	
	
}
