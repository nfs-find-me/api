package com.findme.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		System.out.println("Access denied");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		Map<String, Object> body = new HashMap<>();
		body.put("status", HttpStatus.FORBIDDEN.value());
		body.put("message", "Access denied");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
		
	}
}
