package com.findme.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedException extends Exception {
	
	
	public CustomAccessDeniedException(String message) {
		super(message);
	}
	
	public CustomAccessDeniedException(HttpServletResponse response) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		Map<String, Object> body = new HashMap<>();
		body.put("status", HttpStatus.FORBIDDEN.value());
		body.put("message", "Access denied");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
		
	}
}
