package com.ken.aws.quiz.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class StaticFileService {

	private static final Log LOG = LogFactory.getLog(StaticFileService.class);

	public String readFileToEnd(File file) {
		if (file == null || !file.exists()) {
			return null;
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) file.length());
		try (FileInputStream inputStream = new FileInputStream(file)) {
			byte[] buffer = new byte['?'];
			int count = 0;
			while ((count = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, count);
			}
		} catch (IOException e) {
			LOG.warn(e.getMessage());
		}

		return new String(outputStream.toByteArray());
	}

}
