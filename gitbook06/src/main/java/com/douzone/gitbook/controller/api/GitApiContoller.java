package com.douzone.gitbook.controller.api;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.AlarmService;
import com.douzone.gitbook.service.GitService;
import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.util.LinuxServer;
import com.douzone.gitbook.vo.AlarmVo;
import com.douzone.gitbook.vo.GitVo;
import com.douzone.gitbook.vo.UserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.saro.commons.ssh.SSHExecutor;

@Controller("RepositoryApiController")
@RequestMapping("/Repository/{id:(?!assets).*}")
public class GitApiContoller {
	private final static String host = "192.168.1.15";
	private final static int port = 22;
	private final static String user = "gitbook";
	private final static String password = "gitbook";
	private final static String charset = "utf-8";

	private final static String dir = "/var/www/git/gitbook/";

	@Autowired
	private GitService gitService;

	@Autowired
	private UserService userService;

	@Autowired
	private AlarmService alarmService;

	@Autowired
	private ObjectMapper jsonMapper;

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonResult RepositoryList(@PathVariable String id) {
		List<GitVo> list = gitService.getRepositoryList(id);
		return JsonResult.success(list);
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public JsonResult RepositoryDelete(@PathVariable String id, @RequestBody GitVo vo) {

		gitService.deleteRepository(id, vo);
		List<GitVo> list = gitService.getRepositoryList(id);
		return JsonResult.success(list);
	}

	@PostMapping("/add")
	public void add(@RequestBody GitVo vo, @PathVariable String id) {

		vo.setGitName(vo.getGitName().trim());

		try {
			SSHExecutor.just(host, port, user, password, charset,
					"cd " + dir + id + " && sudo git-create-repo " + id + " " + vo.getGitName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		gitService.insertGit(vo);

	}

	@ResponseBody
	@RequestMapping("/update")
	public JsonResult updateVisible(@PathVariable String id, @RequestBody GitVo vo) {

		System.out.println("update:" + vo);
		gitService.updateVisible(vo);

		List<GitVo> list = gitService.getRepositoryList(id);
		System.out.println("list:" + list);

		return JsonResult.success(list);
	}

	@ResponseBody
	@GetMapping("/item/{repoName}")
	public JsonResult gitListItem(@PathVariable String id, @PathVariable("repoName") String repoName) {
		GitVo vo = gitService.getGitItem(id, repoName);
		return JsonResult.success(vo);
	}

	@ResponseBody
	@GetMapping("/repolist/{repoName}")
	public JsonResult showRootOnRepo(@PathVariable String id, @PathVariable("repoName") String repoName)
			throws NoSuchAlgorithmException {

		// 잘못된 URL 입력
		if (gitService.checkUserAndRepo(id, repoName) == false) {
			return JsonResult.fail("repo not found");
		}

		// 최초 만든 repo일 경우
		if (GitService.checkNewRepo(id, repoName).contains("fatal: Not a valid object name master")) {
			return JsonResult.fail("newRepo");
		}
		System.out.println("레포지토리 생성 실행");
		return JsonResult.success(GitService.getFileListOnTop(id, repoName));
	}

	@ResponseBody
	@GetMapping("/repolist/{repoName}/**")
	public JsonResult showInternalOnRepo(@PathVariable String id, @PathVariable("repoName") String repoName,
			HttpServletRequest request) {
		String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

		String pathName = fullPath.substring(fullPath.indexOf(repoName) + repoName.length() + 1);

		if ("/".equals(pathName.substring(pathName.length() - 1, pathName.length()))) {
			pathName = pathName.substring(0, pathName.length() - 1);
		}

		Map<String, Object> data = GitService.getView(id, repoName, pathName);
		if (data == null) {
			return JsonResult.fail("cannot retrieve internal contents");
		}

		return JsonResult.success(data);
	}

	@ResponseBody
	@RequestMapping(value = "/checkPW")
	public JsonResult checkEmail(@PathVariable String id, @RequestBody String password) {
		boolean exist = userService.existUser(password, id);

		return JsonResult.success(exist);
	}

	@ResponseBody
	@RequestMapping(value = "/pushProcess", method = RequestMethod.POST)
	public JsonResult pushProcess(@RequestBody Map<String, Object> input, @PathVariable("id") String id) {
		String[] commitMsgList = LinuxServer.getResult("cd /var/www/git/" + input.get("repo")
				+ " && git log --date=iso8601 --pretty=format:\"%H<<>>%ad<<>>%s\" | grep " + input.get("commit"))
				.split("\\<<>>");

		Map<String, Object> push = new HashMap<>();
		push.put("id", (String) input.get("username"));
		push.put("repoName", ((String) input.get("repo")).split("/")[2].split("\\.")[0]);
		push.put("commitMsg", commitMsgList[2]);
		push.put("commitDate", commitMsgList[1].split("\\+")[0].split(" ")[0]);
		
		long userNo = userService.getUserNo((String)push.get("id"));
		
		push.put("groupNo", gitService.getGroupNo( (String)push.get("repoName") , (String)push.get("id"), userNo ) );
		
		push.put("contents", "COMMIT UPDATE!!\n\n\n[" + push.get("repoName") + ".git]에 Commit하였습니다.\nCommit Message : " + push.get("commitMsg"));
		push.put("contents_short", push.get("repoName") + ">>>>>" + push.get("commitMsg"));

		Boolean result = gitService.pushProcess(push);
		if (!result) {
			return JsonResult.fail("failed for updating push records");
		}
		
		AlarmVo alarmVo = new AlarmVo();
		alarmVo.setUserId((String) push.get("id"));
		alarmVo.setAlarmType("commit");
		alarmVo.setAlarmContents((String) push.get("contents"));
			
		
		AlarmVo recentAlarm = alarmService.getRecentAlarm(alarmVo);
		recentAlarm.setGroupNo((Long) push.get("groupNo"));
		recentAlarm.setRepoName((String)push.get("repoName"));
				
		try {
			String alarmJsonStr = jsonMapper.writeValueAsString(recentAlarm);
			alarmService.sendAlarm("alarm>>" + alarmJsonStr, (String) push.get("id"));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return JsonResult.success(true);
	}

	@ResponseBody
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public JsonResult checkRepositoryName(HttpServletRequest request) {
		HttpSession httpSession = request.getSession(false);
		UserVo uservo = (UserVo) httpSession.getAttribute("authUser");

		System.out.println("네임 중복 체크" + uservo.getId());
		List<GitVo> list = gitService.getMyRepositoryList(uservo.getId());
		return JsonResult.success(list);
	}

	@ResponseBody
	@RequestMapping(value = "/grouplist/{groupno}/{userno}", method = RequestMethod.GET)
	public JsonResult groupRepositoryList(@PathVariable String id, @PathVariable Long groupno,
			@PathVariable Long userno) {
		System.out.println("group git chk : " + id + ":" + groupno.toString() + ":" + userno.toString());
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("groupNo", groupno.toString());
		map.put("userNo", userno.toString());

		List<GitVo> list = gitService.getGroupRepositoryList(map);
		System.out.println("fdaf :" + list.size());

		return JsonResult.success(list);
	}

	@ResponseBody
	@RequestMapping(value = "/group/delete", method = RequestMethod.POST)
	public JsonResult groupRepositoryDelete(@PathVariable String id, @RequestBody GitVo vo) {

		gitService.deleteRepository(id, vo);
		Map<String, String> map = new HashMap<String, String>();
		map.put("groupNo", vo.getGroupNo().toString());
		map.put("userNo", vo.getUserNo().toString());
		
		List<GitVo> list = gitService.getGroupRepositoryList(map);
		return JsonResult.success(list);
	}

	@ResponseBody
	@RequestMapping("/group/update")
	public JsonResult groupUpdateVisible(@PathVariable String id, @RequestBody GitVo vo) {

		System.out.println("update:" + vo);
		gitService.updateVisible(vo);

		Map<String, String> map = new HashMap<String, String>();
		map.put("groupNo", vo.getGroupNo().toString());
		map.put("userNo", vo.getUserNo().toString());

		List<GitVo> list = gitService.getGroupRepositoryList(map);
		System.out.println("list:" + list);

		return JsonResult.success(list);
	}

	@ResponseBody
	@GetMapping("/group/repolist/{repoName}")
	public JsonResult showRootOnRepoGroup(@PathVariable String id, @PathVariable("repoName") String repoName)
			throws NoSuchAlgorithmException {
		String userid = userService.getUserId(id);
		System.out.println("repo test : " + userid);
		// 잘못된 URL 입력
		if (gitService.checkUserAndRepo(userid, repoName) == false) {
			return JsonResult.fail("repo not found");
		}

		// 최초 만든 repo일 경우
		if (GitService.checkNewRepo(userid, repoName).contains("fatal: Not a valid object name master")) {
			return JsonResult.fail("newRepo");
		}
		System.out.println("레포지토리 생성 실행");
		return JsonResult.success(GitService.getFileListOnTop(userid, repoName));
	}

	@ResponseBody
	@GetMapping("/group/repolist/{repoName}/**")
	public JsonResult showInternalOnRepoGroup(@PathVariable String id, @PathVariable("repoName") String repoName,
			HttpServletRequest request) {
		String userid = userService.getUserId(id);
		System.out.println("repo test2 : " + userid);

		String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

		String pathName = fullPath.substring(fullPath.indexOf(repoName) + repoName.length() + 1);

		if ("/".equals(pathName.substring(pathName.length() - 1, pathName.length()))) {
			pathName = pathName.substring(0, pathName.length() - 1);
		}

		Map<String, Object> data = GitService.getView(userid, repoName, pathName);
		if (data == null) {
			return JsonResult.fail("cannot retrieve internal contents");
		}

		return JsonResult.success(data);
	}

	@ResponseBody
	@GetMapping("/group/item/{repoName}")
	public JsonResult gitListItemGroup(@PathVariable String id, @PathVariable("repoName") String repoName) {
		String userid = userService.getUserId(id);
		System.out.println("repo item : " + userid);

		GitVo vo = gitService.getGitItem(userid, repoName);
		return JsonResult.success(vo);
	}

}
