package com.findme.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileService {
	
	public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
		File file = new File(multipartFile.getOriginalFilename()); // Créez un fichier temporaire
		
		try (OutputStream os = new FileOutputStream(file);
			 InputStream is = multipartFile.getInputStream()) {
			int bytesRead;
			byte[] buffer = new byte[8192];
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			throw e;
		}
		
		return file;
	}
}
