package com.douzone.gitbook.controller.api;



import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.GitService;
import com.douzone.gitbook.vo.GitVo;

import com.douzone.security.AuthUser;

import me.saro.commons.ssh.SSHExecutor;

@Controller("RepositoryApiController")
@RequestMapping("/Repository/{id:(?!assets).*}")
public class GitApiContoller {
	private final static String host = "192.168.1.15";
	private final static int port = 22;
	private final static String user = "gitbook";
	private final static String password = "gitbook";
	private final static String charset = "utf-8";
	private final static String dir = "/var/www/git/" ;
	
	
	@Autowired
	private GitService gitService;
	
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public JsonResult RepositoryList(
		@PathVariable String id	
			) {
		List<GitVo> list = gitService.getRepositoryList(id);
		return JsonResult.success(list);
	}
	
	
	@PostMapping("/add")
	public void add(@RequestBody GitVo vo) {
		vo.setGitName(vo.getGitName().trim());
		
		try {
			SSHExecutor.just(host, port, user, password, charset,
					"cd " + dir + "user05" + " && sudo git-create-repo " + "user05" + " " + vo.getGitName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		gitService.insertGit(vo);
		
		
	}
	
	@PostMapping("/update")
	public void updateVisible(
			@PathVariable String id,
			@RequestBody GitVo vo
			) {
		
		System.out.println("add:"+vo);
		gitService.updateVisible(vo);
		
	}
	
	
}
