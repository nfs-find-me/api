package com.findme.api.controller;

import com.findme.api.exception.CustomAccessDeniedException;
import com.findme.api.exception.CustomUnauthorizedException;
import com.findme.api.model.AuthRequest;
import com.findme.api.model.User;
import com.findme.api.response.ResponseJson;
import com.findme.api.service.JwtService;
import com.findme.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService userService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/register")
	public ResponseJson<User> register(@RequestBody AuthRequest authRequest) throws CustomUnauthorizedException {
		return new ResponseJson<>(userService.register(authRequest), HttpStatus.OK.value());
	}
	
	@PostMapping("/login")
	public ResponseJson<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
		);
		return new ResponseJson<>(jwtService.generateToken(authRequest.getUsername()), HttpStatus.OK.value(), jwtService.getExpiration());
	}
	@GetMapping("/mail-verif")
	public boolean checkMail(@Param("id") String id, @Param("code") String code) throws Exception {
		return userService.checkMail(id,code);
	}
	@GetMapping("/send-mail-verif")
	public User GenerateMailCode(@Param("email")  String email) throws Exception {
		return userService.generateNewCode(email);
	}
}
