package com.findme.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomException extends Exception {
	
	private System.Logger logger = System.getLogger(CustomException.class.getName());
	
	public CustomException() {
		super();
	}
	
	public CustomException(HttpServletResponse response, HttpStatus status, String message) throws IOException {
		logger.log(System.Logger.Level.ERROR, "CustomException: " + message);
		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		Map<String, Object> body = new HashMap<>();
		body.put("status", status.value());
		body.put("message", message != null ? message : status.getReasonPhrase());
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
		
	}
}
