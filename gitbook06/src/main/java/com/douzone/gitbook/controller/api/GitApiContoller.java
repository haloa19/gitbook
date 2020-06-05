package com.douzone.gitbook.controller.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.GitService;

import com.douzone.gitbook.service.UserService;

import com.douzone.gitbook.util.LinuxServer;

import com.douzone.gitbook.vo.GitVo;

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
			SSHExecutor.just(host, port, user, password, charset, "cd " + dir + id + " && sudo git-create-repo " + id + " " + vo.getGitName());
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
	public JsonResult showRootOnRepo(@PathVariable String id, @PathVariable("repoName") String repoName) {

		// 잘못된 URL 입력
		if (GitService.checkUserAndRepo(id, repoName) == false) {
			return JsonResult.fail("repo not found");
		}

		// 최초 만든 repo일 경우
		if (GitService.checkNewRepo(id, repoName).contains("fatal: Not a valid object name master")) {
			return JsonResult.fail("newRepo");
		}

		return JsonResult.success(GitService.getFileListOnTop(id, repoName));
	}

	@ResponseBody
	@GetMapping("/repolist/{repoName}/**")
	public JsonResult showInternalOnRepo(@PathVariable String id, @PathVariable("repoName") String repoName, HttpServletRequest request) {
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
		@RequestMapping(value="/checkPW")
		public JsonResult checkEmail(@PathVariable String id,
				@RequestBody String password) {
			boolean exist = userService.existUser(password,id);
		
			return JsonResult.success(exist);
		}

	@ResponseBody
	@RequestMapping(value = "/pushProcess", method = RequestMethod.POST)
	public JsonResult pushProcess(@RequestBody Map<String, Object> input, @PathVariable("id") String id) {
		String[] commitMsgList = LinuxServer.getResult("cd /var/www/git/" + input.get("repo") + " && git log --date=iso8601 --pretty=format:\"%H<<>>%ad<<>>%s\" | grep " + input.get("commit"))
				.split("\\<<>>");

		Map<String, Object> push = new HashMap<>();
		push.put("id", (String) input.get("username"));
		push.put("repo", ((String) input.get("repo")).split("/")[2]);
		push.put("commitMsg", commitMsgList[2]);
		push.put("commitDate", commitMsgList[1].split("\\+")[0].split(" ")[0]);

		push.put("contents", push.get("id") + " 님이 " + push.get("repo") + " 에 PUSH 하였습니다. (" + push.get("commitMsg") + ")");
		push.put("contents_short", push.get("repo") + " : \"" + push.get("commitMsg") + "\"");

		Boolean result = gitService.pushProcess(push);
		if (!result) {
			return JsonResult.fail("failed for updating push records");
		}
		return JsonResult.success(true);
	}


}
