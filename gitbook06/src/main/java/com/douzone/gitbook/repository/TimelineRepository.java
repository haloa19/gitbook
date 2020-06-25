package com.douzone.gitbook.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.gitbook.vo.CommentVo;
import com.douzone.gitbook.vo.FileVo;
import com.douzone.gitbook.vo.LikeVo;
import com.douzone.gitbook.vo.TagVo;
import com.douzone.gitbook.vo.TimelineVo;
import com.douzone.gitbook.vo.UserVo;

@Repository
public class TimelineRepository {
	@Autowired
	private SqlSession sqlSession;

	public void insertTimeline(TimelineVo vo) {
		sqlSession.insert("timeline.insertTimeline", vo);
	}

	public void insertTimelineFile(String url, Long no) {
		Map<String, Object> map = new HashMap<>();
		map.put("url", url);
		map.put("no", no);

		sqlSession.insert("timeline.insertTimelineFile", map);
	}

	public void insertTag(TagVo tagVo) {
		sqlSession.insert("timeline.insertTag", tagVo);
	}

	public void insertTagList(Long timelineNo, Long tagNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("timelineNo", timelineNo);
		map.put("tagNo", tagNo);
		sqlSession.insert("timeline.insertTagList", map);
	}

	public List<TimelineVo> getMyTimelineList(String id) {
		return sqlSession.selectList("timeline.getMyTimelineList", id);
	}

	public UserVo getUserInfo(Long userNo) {

		return sqlSession.selectOne("timeline.getUserInfo", userNo);
	}

	public List<FileVo> getFileList(Long timelineNo) {

		return sqlSession.selectList("timeline.getFileList", timelineNo);
	}

	public List<TagVo> getTagList(Long timelineNo) {
		return sqlSession.selectList("timeline.getTagList", timelineNo);
	}

	public List<CommentVo> getCommentList(Long timelineNo) {
		return sqlSession.selectList("timeline.getCommentList", timelineNo);
	}

	public List<LikeVo> getLikeList(Long timelineNo) {
		return sqlSession.selectList("timeline.getLikeList", timelineNo);
	}

	public void addlike(LikeVo vo) {
		sqlSession.insert("timeline.addlike", vo);
	}

	public void deletelike(LikeVo vo) {
		sqlSession.delete("timeline.deletelike", vo);
	}

	public void insertComment(CommentVo newComment) {
		sqlSession.insert("timeline.insertComment", newComment);
	}

	public void deleteComment(Long commentNo) {
		sqlSession.delete("timeline.deleteComment", commentNo);
	}

	public List<TimelineVo> getMainTimelineList(UserVo vo) {
		return sqlSession.selectList("timeline.getMainTimelineList", vo);
	}

	public void updateTimeline(TimelineVo vo) {
		sqlSession.update("timeline.updateTimeline", vo);
	}

	public void deleteTag(Long timelineNo) {
		sqlSession.delete("timeline.deleteTag", timelineNo);
	}

	public void deleteFile(Long timelineNo) {
		sqlSession.delete("timeline.deleteFile", timelineNo);
	}

	public void deleteTimeline(Long timelineNo) {
		sqlSession.delete("timeline.deleteTimeline", timelineNo);
	}

	public List<TimelineVo> getTagTimelineList(String tagid) {
		Map<String, Object> map = new HashMap<>();
		map.put("tagid", tagid);
		return sqlSession.selectList("timeline.getTagTimelineList", map);
	}

	public List<TimelineVo> getGroupTimelineList(Map<String, Long> map) {
		return sqlSession.selectList("timeline.getGroupTimelineList", map);
	}

	public void deleteGroupall(Long no) {
		sqlSession.delete("timeline.deleteGroupAll", no);

	}
}
