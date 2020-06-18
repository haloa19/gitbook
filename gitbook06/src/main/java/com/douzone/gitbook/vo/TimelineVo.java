package com.douzone.gitbook.vo;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class TimelineVo {

	private Long no;
	private String contents;
	private String visible;
	private Long userNo;
	private Long groupNo;
	private String type;

	private String regDate;
	private List<String> tagList;
	private MultipartFile[] sendimgList;
	private int like;

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

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public MultipartFile[] getSendimgList() {
		return sendimgList;
	}

	public void setSendimgList(MultipartFile[] sendimgList) {
		this.sendimgList = sendimgList;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	@Override
	public String toString() {
		return "TimelineVo [no=" + no + ", contents=" + contents + ", visible=" + visible + ", userNo=" + userNo
				+ ", groupNo=" + groupNo + ", type=" + type + ", regDate=" + regDate + ", tagList=" + tagList
				+ ", sendimgList=" + Arrays.toString(sendimgList) + ", like=" + like + "]";
	}
}
