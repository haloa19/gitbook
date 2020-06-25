package com.douzone.gitbook.util;

import java.io.IOException;

import me.saro.commons.ssh.SSHExecutor;

public class LinuxServer {

	private static final String HOST = "192.168.1.15";
	private static final int PORT = 22;
	private static final String USER = "gitbook";
	private static final String PASSWORD = "gitbook";
	private static final String CHARSET = "utf-8";

	public static String getResult(String command) {
		try {
			return SSHExecutor.just(HOST, PORT, USER, PASSWORD, CHARSET, command);
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}

}
