package com.douzone.gitbook.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.gitbook.repository.GitRepository;
import com.douzone.gitbook.vo.GitVo;

import me.saro.commons.ssh.SSHExecutor;

@Service
public class GitService {
	private final static String host = "192.168.1.15";
	private final static int port = 22;
	private final static String user = "gitbook";
	private final static String password = "gitbook";
	private final static String charset = "utf-8";
	private final static String dir = "/var/www/git/gitbook/";

	@Autowired
	private GitRepository gitRepository;

	public List<GitVo> getRepositoryList(String id) {
		return gitRepository.findList(id);
	}

	public void insertGit(GitVo vo) {
		gitRepository.insertGit(vo);
	}

	public void updateVisible(GitVo vo) {
		gitRepository.updateVisible(vo);
	}

	// 사용자 폴더 & 깃 레포지토리 폴더 여부 확인하기 (input 에서 레포지토리 명 뒤에 ".git" 붙이지 않는다)
	// ex) checkUserAndRepo("user03", "test09") <-- .../user03/test09.git/ 여부 확인
	public Boolean checkUserAndRepo(String userName, String repoName) throws NoSuchAlgorithmException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		java.security.KeyPairGenerator.getInstance("DH");
		javax.crypto.KeyAgreement.getInstance("DH");

		return GitService.getResult("ls " + dir + userName + "/" + repoName + ".git")
				.contains("ls: cannot access") == false;
	}

	public static String getResult(String command) {
		try {
			return SSHExecutor.just(host, port, user, password, charset, command);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String checkNewRepo(String userName, String repoName) {
		String result = null;
		try {
			result = SSHExecutor.just(host, port, user, password, charset,
					"cd " + dir + userName + "/" + repoName + ".git && git ls-tree --full-tree --name-status master");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/*
	 * 한 사용자/레포지토리의 파일/폴더 목록 제일 상단을 보여주기 [조건] RepositoryController에서
	 * checkUserAndRepo(userName, repoName) == true 임을 확인했다는 전제 하에 실행한다 !! dat {
	 * type: "folder", contents: { '0' : { path: commit: date: }, '1' : { path:
	 * commit: date: }, ... } }
	 */
	public static Map<String, Object> getFileListOnTop(String userName, String repoName) {
		Map<String, Object> result = new HashMap<>();
		result.put("type", "folder");

		Map<String, Object> contents = new HashMap<>();

		// contents 에다가 넣을 내용들을 가져온다.
		// contents 에다가 넣을 내용들을 가져온다.
		String[] fileList = GitService
				.getResult(
						"sudo python3 /root/gitserver/script/git-view-list.py " + userName + " " + repoName + " master")
				.split("\n");

		for (int i = 0; i < fileList.length; i++) {
			// 임시 버퍼 Map을 정의
			Map<String, Object> buffer = new HashMap<>();
			String[] splited = fileList[i].split("\t");

			// 1. 경로 path를 넣기
			buffer.put("path", splited[0]);

			// 2. 경로 path에 해당되는 commit 메시지를 넣기
			// 해당 path의 commit 메시지 가져오기

			buffer.put("commit", splited[1]);

			// 3. 경로 path의 일자 정보 넣기
			// 해당 path의 date 가져오기

			buffer.put("date", splited[2]);

			buffer.put("type", splited[3]);

			// 4. 마지막으로 contents 에다가 {"i" : buffer 객체} 형태로 넣는다.
			contents.put(String.valueOf(i), buffer);
		}

		// contents 이름의 HashMap 객체를 넣기
		result.put("contents", contents);

		return result;
	}

	public GitVo getGitItem(String id, String repoName) {
		return gitRepository.getGitItem(id, repoName);
	}

	public void deleteRepository(String id, GitVo vo) {
		try {
			gitRepository.deleteRepository(vo);
			SSHExecutor.just(host, port, user, password, charset,
					"cd " + dir + id + " && sudo rm -rf " + vo.getGitName() + ".git");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Object> getView(String userName, String repoName, String pathName) {
		// 1. path가 파일(blob) 인지 아니면 폴더(tree)인지 확인하기
		// [주의] path 끝에 "/" 뺀다 !!
		// infoLine[0] : 유형코드
		// infoLine[1] : 유형(blob/tree)
		// infoLine[2] : 해시코드
		// infoLine[3] : 파일 or 폴더 (경로 포함, 빈칸 있으면 잘릴 수도 있음...어차피 안쓰임)
		String infoLine = GitService.getResult("cd " + dir + userName + "/" + repoName
				+ ".git && git ls-tree --full-tree master " + pathName.replaceAll(" ", "\\\\ "));

		// 2. 조회되지 않은 경우(== 없다는 의미임) --> null을 보낸다
		if (infoLine == null || "".equals(infoLine)) {
			return null;
		}

		Map<String, Object> result = new HashMap<>();
		String[] info = infoLine.split("\\s+");

		// 3-1. 파일(blob)인 경우
		if ("blob".equals(info[1])) {
			result.put("type", "file");
			result.put("contents", GitService
					.getResult("cd " + dir + userName + "/" + repoName + ".git && git cat-file -p " + info[2]));
		}
		// 3-2. 폴더(tree)인 경우
		else if ("tree".equals(info[1])) {

			result.put("type", "folder");
			Map<String, Object> contents = new HashMap<>();

			// contents 에다가 넣을 내용들을 가져온다.
			String[] fileList = GitService.getResult("sudo python3 /root/gitserver/script/git-view-list.py " + userName
					+ " " + repoName + " master " + pathName).split("\n");

			for (int i = 0; i < fileList.length; i++) {
				// 임시 버퍼 Map을 정의
				Map<String, Object> buffer = new HashMap<>();
				String[] splited = fileList[i].split("\t");

				// 1. 경로 path를 넣기
				buffer.put("path", splited[0]);

				// 2. 경로 path에 해당되는 commit 메시지를 넣기
				// 해당 path의 commit 메시지 가져오기

				buffer.put("commit", splited[1]);

				// 3. 경로 path의 일자 정보 넣기
				// 해당 path의 date 가져오기

				buffer.put("date", splited[2]);

				buffer.put("type", splited[3]);

				// 4. 마지막으로 contents 에다가 {"i" : buffer 객체} 형태로 넣는다.
				contents.put(String.valueOf(i), buffer);
			}

			// contents 이름의 HashMap 객체를 넣기
			result.put("contents", contents);
		}
		return result;
	}

	public Boolean pushProcess(Map<String, Object> push) {
		return gitRepository.addPushInfo(push);
	}

	public String getNickName(String id) {
		return gitRepository.getUserNickName(id);
	}

	public List<GitVo> getGroupRepositoryList(Map<String, String> map) {
		return gitRepository.findListGroup(map);
	}

	public List<GitVo> getMyRepositoryList(String id) {
		return gitRepository.findMyList(id);
	}

	public void deleteGroupAll(Long no) {
		gitRepository.deleteGroupAll(no);
	}

	public Object getGroupNo(String repoName, String id, long userNo) {
		return gitRepository.getGroupNo(repoName, id, userNo);
	}

	public Long getGroupNo(Map<String, Object> push) {
		return gitRepository.findGroupNo(push);
	}

	public List<String> getGroupMemberIdList(Long groupNo) {
		return gitRepository.findGroupMemberIdList(groupNo);
	}

	public GitVo getGitInfoByNo(Long alarmRefNo) {
		return gitRepository.findGitInfoByNo(alarmRefNo);
	}

}
