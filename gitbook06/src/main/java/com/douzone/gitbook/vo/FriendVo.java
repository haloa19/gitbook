package com.douzone.gitbook.vo;

import org.hibernate.validator.constraints.Length;

public class FriendVo {
	private Long no;

	@Length(min=2, max=8)
	private String id;
	
	@Length(min=4, max=16)
	private String password;
	private String phone; 
	private String name; 
	private String gender;
	private String birthday;
	private String joinDate;
	
	//Profile vo 
	private Long ProfileNo;
	private String image; 
	private String nickname; 
	private String profileContents;
	
	//Friend 상태
	private String status;
	
	//GroupNo
	private Long groupNo;
	

	public Long getNo() {
		return no;
	}
	
	public void setNo(Long no) {
		this.no = no;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public Long getProfileNo() {
		return ProfileNo;
	}
	public void setProfileNo(Long profileNo) {
		ProfileNo = profileNo;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getProfileContents() {
		return profileContents;
	}
	public void setProfileContents(String profileContents) {
		this.profileContents = profileContents;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}	
	
}
