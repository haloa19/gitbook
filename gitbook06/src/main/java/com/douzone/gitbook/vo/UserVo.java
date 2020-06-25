package com.douzone.gitbook.vo;

public class UserVo {
	private Long no;

	private String id;

	private String password;
	private String phone;
	private String name;
	private String gender;
	private String birthday;
	private String joinDate;
	private String userStatus;

	// Profile vo
	private Long ProfileNo;
	private String image;
	private String nickname;
	private String profileContents;

	// Friend
	private Long friendNum;

	// Group
	private String groupTitle;

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

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
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

	public Long getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(Long friendNum) {
		this.friendNum = friendNum;
	}

	public String getGroupTitle() {
		return groupTitle;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	@Override
	public String toString() {
		return "UserVo [no=" + no + ", id=" + id + ", password=" + password + ", phone=" + phone + ", name=" + name
				+ ", gender=" + gender + ", birthday=" + birthday + ", joinDate=" + joinDate + ", ProfileNo="
				+ ProfileNo + ", image=" + image + ", nickname=" + nickname + ", profileContents=" + profileContents
				+ ", friendNum=" + friendNum + ", groupTitle=" + groupTitle + "]";
	}

}
