package com.douzone.gitbook.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.gitbook.repository.GroupRepository;
import com.douzone.gitbook.vo.FriendVo;
import com.douzone.gitbook.vo.GroupVo;
import com.douzone.gitbook.vo.UserVo;

@Service
public class GroupService {
	private static final String SAVE_PATH = "/gitbook-uploads"; 
	private static final String URL = "/gitbook/assets/image";
	
	@Autowired
	private GroupRepository groupRepository;

	public List<GroupVo> getList(UserVo uservo) {
		
		return groupRepository.groupList(uservo);
	}

	public void regist(GroupVo groupVo) {

		groupRepository.groupRegist(groupVo);
	}

	public void grant(Map<String, String> map) {
		
		groupRepository.groupGrant(map);
	}

	public String restore(MultipartFile multipartFile) {
		
		String url = "";
		
		try {
			if(multipartFile.isEmpty()) {
				return url;
			}
			
			String originFilename = multipartFile.getOriginalFilename();
			
			String extName = originFilename.substring(originFilename.lastIndexOf(".") + 1);
			
			String saveFilenmae = generateSaveFilename(extName);
			long fileSize = multipartFile.getSize();
			
			System.out.println("##### file " + originFilename);
			System.out.println("##### file " + saveFilenmae);
			System.out.println("##### size " + fileSize);
			
			byte[] fileData = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFilenmae);
			os.write(fileData);
			os.close();
			
			url = URL + "/" + saveFilenmae; 
			
			
		} catch(IOException ex) {
			throw new RuntimeException("file upload error:" + ex);
		}
		
		return url;
	}
	
	private String generateSaveFilename(String extName) {
		String filename = "";
		Calendar calendar = Calendar.getInstance();
		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += ("." + extName); // 기존 확장자
		
		return filename;
	}

	public GroupVo getInfo(Map<String, Object> param) {
		
		return groupRepository.groupInfo(param);
	}

	public List<FriendVo> getReqGroupList(Map<String, Object> param) {
		
		return groupRepository.reqGroupList(param);
	}
	
	public List<FriendVo> getJoinGroupList(Map<String, Object> param) {
		return groupRepository.joinGroupList(param);
	}

	public void addMember(Map<String, Object> param) {
		groupRepository.addMember(param);
	}

	public List<GroupVo> getMyRequest(UserVo uservo) {
		
		return groupRepository.myreqList(uservo);
		
	}

	public void addGroup(Map<String, Object> param) {
		
		groupRepository.addGroup(param);
	}

	public void rejectGroup(Map<String, Object> param) {
		groupRepository.rejectGroup(param);
		
	}

	public void update(Map<String, Object> param) {
		groupRepository.update(param);
		
	}

	public List<GroupVo> getGrantAll(Long no) {
		
		return groupRepository.getGrantAll(no);
	}

	public void deleteGroupListAllAdmin(Long no) {
		groupRepository.deleteGroupListAllAdmin(no);
		
	}
	
	public void deleteGroupListAll(Long groupno, Long userno) {
		groupRepository.deleteGroupListAll(groupno, userno);
		
	}

	public void deleteGroupAll(Long no) {
		groupRepository.deleteGroupAll(no);
		
	}

	public String getGroupTitle(Long groupNo) {
			return groupRepository.getGroupTitle(groupNo);
	}

}
