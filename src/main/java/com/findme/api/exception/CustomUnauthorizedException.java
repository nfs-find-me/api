package com.findme.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomUnauthorizedException extends Exception {
	
	private String message;
	
	public CustomUnauthorizedException(String message) {
		super();
		this.message = message;
	}
	
	public CustomUnauthorizedException(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("Unauthorized");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		Map<String, Object> body = new HashMap<>();
		body.put("status", HttpStatus.UNAUTHORIZED.value());
		body.put("message", "Unauthorized");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
		
	}
}
