package com.findme.api.model;

import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class VerifyToken {
	
	@Id
	private String id;
	
	@NonNull
	private String userId;
	
	@NonNull
	private String token;
	
	
	private LocalDateTime exp = LocalDateTime.now().plusHours(24);
}
