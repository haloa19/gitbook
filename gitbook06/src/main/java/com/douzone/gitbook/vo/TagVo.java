package com.douzone.gitbook.vo;

public class TagVo {
	private Long no;
	private String tagContents;
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getTagContents() {
		return tagContents;
	}
	public void setTagContents(String tagContents) {
		this.tagContents = tagContents;
	}
	@Override
	public String toString() {
		return "TagVo [no=" + no + ", tagContents=" + tagContents + "]";
	}
	

	
}

