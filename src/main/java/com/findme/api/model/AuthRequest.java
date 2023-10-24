package com.findme.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
	
	private String username ;
	private String email;
	private String password;
	
	private String login;
	private String refreshToken;
}