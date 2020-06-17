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

import com.douzone.gitbook.repository.TimelineRepository;
import com.douzone.gitbook.vo.CommentVo;
import com.douzone.gitbook.vo.FileVo;
import com.douzone.gitbook.vo.GitVo;
import com.douzone.gitbook.vo.LikeVo;
import com.douzone.gitbook.vo.TagVo;
import com.douzone.gitbook.vo.TimelineVo;
import com.douzone.gitbook.vo.UserVo;




@Service
public class TimelineService {
	private static final String SAVE_PATH="/gitbook-uploads";
	private static final String URL="/gitbook/assets/image";
	@Autowired
	private TimelineRepository timelineRepositroy;
	
	
	public String restore(MultipartFile multipartFile) {
		String url="";
		
		try {
		if(multipartFile.isEmpty()) {
			return url; 
		}
		
		
		String originFilename= multipartFile.getOriginalFilename();
	
		String extName = originFilename.substring(originFilename.lastIndexOf('.')+1);
		
		String saveFilename= generatesaveFilename(extName);
		long fileSize = multipartFile.getSize();
		
		System.out.println("##################"+originFilename);
		System.out.println("##################"+saveFilename);
		System.out.println("##################"+fileSize);
		
		byte[] fileData = multipartFile.getBytes();
		OutputStream os =new FileOutputStream(SAVE_PATH+"/"+saveFilename);
		os.write(fileData);
		os.close();
		url=URL+"/"+ saveFilename;
		
		
		}catch(IOException e) {
			throw new RuntimeException("file upload error:" + e);
		}
		return url;
	}
	private String generatesaveFilename(String extName) {
		String fileName ="";
		Calendar calendar = Calendar.getInstance();
		fileName += calendar.get(Calendar.YEAR);
		fileName += calendar.get(Calendar.MONTH);
		fileName += calendar.get(Calendar.DATE);
		fileName += calendar.get(Calendar.HOUR);
		fileName += calendar.get(Calendar.MINUTE);
		fileName += calendar.get(Calendar.SECOND);
		fileName += calendar.get(Calendar.MILLISECOND);
		fileName += ("."+extName);
		
		
		return fileName;
	}
	public void insertTimeline(TimelineVo vo) {
		timelineRepositroy.insertTimeline(vo);
	}
	public void insertTimelineFile(String url, Long no) {
		timelineRepositroy.insertTimelineFile(url,no);
		
	}
	public void insertTag(TagVo tagVo) {
		timelineRepositroy.insertTag(tagVo);
	}
	public void insertTagList(Long timelineNo, Long tagNo) {
		timelineRepositroy.insertTagList(timelineNo,tagNo);
	}
	public List<TimelineVo> getMyTimelineList(String id) {
		
		return timelineRepositroy.getMyTimelineList(id);
	}
	public UserVo getUserInfo(Long userNo) {
		
		return timelineRepositroy.getUserInfo(userNo);
	}
	public List<FileVo> getFileList(Long timelineNo) {
		
		return timelineRepositroy.getFileList(timelineNo);
	}
	public List<TagVo> getTagList(Long timelineNo) {
		return timelineRepositroy.getTagList(timelineNo);
	}
	public List<CommentVo> getCommentList(Long timelineNo) {
		return timelineRepositroy.getCommentList(timelineNo);
	}
	public List<LikeVo> getLikeList(Long timelineNo) {
		return timelineRepositroy.getLikeList(timelineNo);
	}
	public void addlike(LikeVo vo) {
		timelineRepositroy.addlike(vo);
		
	}
	public void deletelike(LikeVo vo) {
		timelineRepositroy.deletelike(vo);
		
	}
	public void insertComment(CommentVo newComment) {
		timelineRepositroy.insertComment(newComment);
		
	}
	public void deleteComment(Long commentNo) {
		timelineRepositroy.deleteComment(commentNo);
		
	}
	public List<TimelineVo> getMainTimelineList(UserVo vo) {
		
		return timelineRepositroy.getMainTimelineList(vo);
	}
	public void updateTimeline(TimelineVo vo) {
		timelineRepositroy.updateTimeline(vo);
		
	}
	public void deleteTag(Long timelineNo) {
		timelineRepositroy.deleteTag(timelineNo);
		
	}
	public void deleteFile(Long timelineNo) {
		timelineRepositroy.deleteFile(timelineNo);
		
	}
	public void deleteTimeline(Long timelineNo) {
		timelineRepositroy.deleteTimeline(timelineNo);
	}
	public List<TimelineVo> getTagTimelineList(String tagid) {
		return timelineRepositroy.getTagTimelineList(tagid);
	}
	public List<TimelineVo> getGroupTimelineList(Map<String, Long> map) {
		
		return timelineRepositroy.getGroupTimelineList(map);
	}
	public void deleteGroupAll(Long no) {
		timelineRepositroy.deleteGroupall(no);
	}
}
