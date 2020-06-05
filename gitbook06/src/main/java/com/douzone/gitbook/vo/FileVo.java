package com.douzone.gitbook.vo;

public class FileVo {
	private Long no;
	private Long timelineNo;
	private String fileContents;
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public Long getTimelineNo() {
		return timelineNo;
	}
	public void setTimelineNo(Long timelineNo) {
		this.timelineNo = timelineNo;
	}
	public String getFileContents() {
		return fileContents;
	}
	public void setFileContents(String fileContents) {
		this.fileContents = fileContents;
	}
	@Override
	public String toString() {
		return "FileVo [no=" + no + ", timelineNo=" + timelineNo + ", fileContents=" + fileContents + "]";
	}
	
	
}
