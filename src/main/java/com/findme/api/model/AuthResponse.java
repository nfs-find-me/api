package com.findme.api.model;

import lombok.Data;

@Data
public class AuthResponse {
	public String jwtToken;
	
	public String refreshToken;
}
