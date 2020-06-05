package com.douzone.gitbook.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	private static final String SAVE_PATH = "/gitbook-uploads";
	private static final String URL = "/gitbook/assets/image";

	public String restore(MultipartFile multipartFile) {
		String newFileName = "";

		try {
			if (multipartFile.isEmpty()) {
				return newFileName;
			}
			String originFilename = multipartFile.getOriginalFilename();
			String extName = originFilename.substring(originFilename.lastIndexOf('.') + 1);
			newFileName = generateSaveFilename(extName);

			System.out.println("Original file name: " + originFilename);

			byte[] fileData = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + newFileName);
			os.write(fileData);
			os.close();

		} catch (IOException ex) {
			throw new RuntimeException("file upload error:" + ex);
		}

		System.out.println("New file name: " + URL + "/" + newFileName);
		return URL + "/" + newFileName;
	}

	private String generateSaveFilename(String extName) {
		String newFileName = "";

		Calendar calendar = Calendar.getInstance();
		newFileName += calendar.get(Calendar.YEAR);
		newFileName += calendar.get(Calendar.MONTH);
		newFileName += calendar.get(Calendar.DATE);
		newFileName += calendar.get(Calendar.HOUR);
		newFileName += calendar.get(Calendar.MINUTE);
		newFileName += calendar.get(Calendar.SECOND);
		newFileName += calendar.get(Calendar.MILLISECOND);
		newFileName += ("." + extName);

		return newFileName;
	}
}
