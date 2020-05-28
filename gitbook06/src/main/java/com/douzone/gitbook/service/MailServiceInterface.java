package com.douzone.gitbook.service;

public interface MailServiceInterface {
	public boolean send(String subject, String text, String from, String to, String filePath);
}
