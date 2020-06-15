package com.douzone.gitbook.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.TimelineService;
import com.douzone.gitbook.vo.CommentVo;
import com.douzone.gitbook.vo.FileVo;
import com.douzone.gitbook.vo.GitVo;
import com.douzone.gitbook.vo.LikeVo;
import com.douzone.gitbook.vo.TagVo;
import com.douzone.gitbook.vo.TimelineVo;
import com.douzone.gitbook.vo.UserVo;
import com.douzone.security.AuthUser;


@RestController
@RequestMapping({"/timeline/{id:(?!assets).*}"})
public class TimelineApiController {

	@Autowired
	private TimelineService timelineService;
	
	

	@PostMapping(value= {"/upload/undefined","/upload/{groupNo}"})
	public void addUpload(
			@PathVariable String id,
			@PathVariable Optional<Long> groupNo,
			@RequestParam(value="userNo", required= true) Long userNo,
			@RequestParam(value="contents", required= true) String contents,
			@RequestParam(value="visible", required= true) String visible,
			@RequestParam(value="tagList", required= true)ArrayList<String> tagList,
			@RequestParam(value="imgList", required= true)ArrayList<String> imgList
			
			) {
		
			TimelineVo vo=new TimelineVo();
			vo.setUserNo(userNo);
			vo.setContents(contents);
			vo.setVisible(visible);
			
			if(groupNo.isPresent()) {
				vo.setGroupNo(groupNo.get());
			}
			
			timelineService.insertTimeline(vo);		
			for(String url: imgList) {	
				timelineService.insertTimelineFile(url,vo.getNo());
			}
			for(String tagContents: tagList) {	
				TagVo tagVo= new TagVo();
				tagVo.setTagContents(tagContents);
				timelineService.insertTag(tagVo);
				timelineService.insertTagList(vo.getNo(),tagVo.getNo());
			}

	}
	
	@PostMapping(value="/update/{timelineNo}")
	public void timelineUpdate(
			@PathVariable String id,
			@PathVariable Long timelineNo,
			@RequestParam(value="userNo", required= true) Long userNo,
			@RequestParam(value="contents", required= true) String contents,
			@RequestParam(value="visible", required= true) String visible,
			@RequestParam(value="tagList", required= true)ArrayList<String> tagList,
			@RequestParam(value="imgList", required= true)ArrayList<String> imgList
			) {
		
		TimelineVo vo=new TimelineVo();
		vo.setNo(timelineNo);
		vo.setUserNo(userNo);
		vo.setContents(contents);
		vo.setVisible(visible);
		
		timelineService.updateTimeline(vo);	
		timelineService.deleteFile(timelineNo);
		timelineService.deleteTag(timelineNo);
		
		
		for(String url: imgList) {	
			timelineService.insertTimelineFile(url,vo.getNo());
		}
		for(String tagContents: tagList) {	
			TagVo tagVo= new TagVo();
			tagVo.setTagContents(tagContents);
			timelineService.insertTag(tagVo);
			timelineService.insertTagList(vo.getNo(),tagVo.getNo());
		}
	}
	
	
	
	@PostMapping(value="/fileupload")
	public JsonResult addUpload(
			@PathVariable String id,
			@RequestParam MultipartFile file
			) {
	String url=	timelineService.restore(file);
	return JsonResult.success(url);
	}
	
	@ResponseBody
	@RequestMapping(value="/list")
	public JsonResult myTimelineList(
			@PathVariable String id
			) {
		List<TimelineVo> list = timelineService.getMyTimelineList(id);
		return JsonResult.success(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/list/{groupNo}")
	public JsonResult groupTimelineList(
			@PathVariable Long id,
			@PathVariable Long groupNo
			) {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("userNo", id);
		map.put("groupNo", groupNo);
		
		List<TimelineVo> list = timelineService.getGroupTimelineList(map);

		return JsonResult.success(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/mainlist")
	public JsonResult mainTimelineList(
			@AuthUser UserVo userVo,
			@PathVariable String id
			) {
			
		List<TimelineVo> list = timelineService.getMainTimelineList(userVo);
		
		return JsonResult.success(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/taglist/{tagid}")
	public JsonResult tagTimelineList(
			@AuthUser UserVo userVo,
			@PathVariable String id,
			@PathVariable String tagid
			) {
	
		List<TimelineVo> list = timelineService.getTagTimelineList(tagid);
		
		return JsonResult.success(list);
	}
	
	
	@RequestMapping(value="/deleteTimeline/{timelineNo}")
	public void deleteTimeline(
			@PathVariable Long timelineNo
			) {
		timelineService.deleteTimeline(timelineNo);
		
		
	}
	
	@ResponseBody
	@GetMapping(value="/info/{userNo}/{timelineNo}")
	public JsonResult timelineUserNo(
			
			@PathVariable Long userNo,
			@PathVariable Long timelineNo
			
			) {
		
		Map<String,Object> map = new  HashMap<>();
		List<FileVo> fileListvo = timelineService.getFileList(timelineNo);
		List<TagVo>  tagListvo=timelineService.getTagList(timelineNo);
		List<CommentVo> commentvo =timelineService.getCommentList(timelineNo);
		List<LikeVo> likevo =timelineService.getLikeList(timelineNo);
		UserVo userInfovo = timelineService.getUserInfo(userNo);
		List<String> url = new ArrayList<>();
		List<String> tag = new ArrayList<>();
		
		for(FileVo fileurl : fileListvo) {
				url.add(fileurl.getFileContents());
		}
		
		for(TagVo tagItme : tagListvo) {
			tag.add(tagItme.getTagContents());
	}
		
		
		map.put("fileList",fileListvo);
		map.put("userInfo",userInfovo);
		map.put("tagList",tagListvo);
		map.put("commentList",commentvo);
		map.put("likeList",likevo);
		map.put("url",url);
		map.put("tag",tag);
		
		
		return JsonResult.success(map);
	}
	
	@ResponseBody
	@GetMapping(value="/addlike/{authUserNo}/{timelineNo}")
	public JsonResult addlike(
		
			@PathVariable Long authUserNo,
			@PathVariable Long timelineNo
			
			) {
		
		LikeVo vo= new LikeVo();
		vo.setUserNo(authUserNo);
		vo.setTimelineNo(timelineNo);
		
		timelineService.addlike(vo);
		
		List<LikeVo> likevo =timelineService.getLikeList(timelineNo);
		return JsonResult.success(likevo);
	}
	
	@ResponseBody
	@GetMapping(value="/deletelike/{authUserNo}/{timelineNo}")
	public JsonResult deletelike(
		
			@PathVariable Long authUserNo,
			@PathVariable Long timelineNo
			
			) {
		
		LikeVo vo= new LikeVo();
		vo.setUserNo(authUserNo);
		vo.setTimelineNo(timelineNo);
		
		timelineService.deletelike(vo);
		
		List<LikeVo> likevo =timelineService.getLikeList(timelineNo);
		return JsonResult.success(likevo);
	}
	
	@ResponseBody
	@PostMapping(value="/addcomment")
	public JsonResult addcomment(
		
			@RequestBody CommentVo newComment
			) {
		timelineService.insertComment(newComment);
		List<CommentVo> commentvo =timelineService.getCommentList(newComment.getTimelineNo());
		return JsonResult.success(commentvo);
	}
	
	@ResponseBody
	@GetMapping(value="/deletecomment/{CommentNo}/{TimelineNo}")
	public JsonResult deletecomment(
		
			@PathVariable Long CommentNo,
			@PathVariable Long TimelineNo
			) {
		timelineService.deleteComment(CommentNo);
		
		List<CommentVo> commentvo =timelineService.getCommentList(TimelineNo);
		return JsonResult.success(commentvo);
	}
	

}
