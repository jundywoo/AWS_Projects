package com.ken.aws.quiz.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class StaticFileService {

	private static final Log LOG = LogFactory.getLog(StaticFileService.class);

	public String readFileToEnd(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		try {
			return readFileToEnd(new FileInputStream(file), file.length());
		} catch (FileNotFoundException e) {
			LOG.warn(e.getMessage());
			return null;
		}
	}

	public String readFileToEnd(Resource resource) {
		if (resource == null) {
			return null;
		}
		try {
			return readFileToEnd(resource.getInputStream(), resource.getFile().length());
		} catch (IOException e) {
			LOG.warn(e.getMessage());
			return null;
		}
	}

	public String readFileToEnd(InputStream inputStream, long length) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) length);
		try {
			byte[] buffer = new byte['?'];
			int count = 0;
			while ((count = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, count);
			}
			return new String(outputStream.toByteArray());
		} catch (IOException e) {
			LOG.warn(e.getMessage());
			return null;
		}
	}

}
