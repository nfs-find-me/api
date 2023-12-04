package com.findme.api.service;

import com.findme.api.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AIService {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private Environment environment;
	
	public CloseableHttpResponse aiResponse(MultipartFile file, HttpServletResponse httpServletResponse) throws IOException, CustomException {
		CloseableHttpClient httpclient = HttpClients.custom()
				.setRedirectStrategy(new LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET and POST methods 
				.build();
		
		HttpPost httppost = new HttpPost(environment.getProperty("ai.url"));
		File convFile = fileService.convertMultipartFileToFile(file);
		HttpEntity entity = MultipartEntityBuilder.create()
				.addBinaryBody("file", convFile, ContentType.create("image/jpeg"), convFile.getName())
				.build();
		
		httppost.setEntity(entity);
		
		CloseableHttpResponse response = httpclient.execute(httppost);
		
		if (response.getStatusLine().getStatusCode() != 200) {
			convFile.delete();
			throw new CustomException(httpServletResponse, HttpStatus.BAD_REQUEST, "Image not uploaded");
		}
		
		HttpEntity responseEntity = response.getEntity();
		
		if (responseEntity != null) {
			InputStream instream = responseEntity.getContent();
			try {
				// do something useful
			} finally {
				instream.close();
			}
		}	
		return response;
	}
}
