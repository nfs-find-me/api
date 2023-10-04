package com.findme.api.controller;

import com.findme.api.model.AuthRequest;
import com.findme.api.model.User;
import com.findme.api.service.JwtService;
import com.findme.api.service.AuthService;
import com.findme.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
	public User register(@RequestBody AuthRequest authRequest) {
		return userService.register(authRequest);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
		);
		return ResponseEntity.ok(jwtService.generateToken(authRequest.getUsername()));
	}
	@GetMapping("/mail-verif")
	public boolean checkMail(@Param("id") String id, @Param("code") String code) throws Exception {
		return userService.checkMail(id,code);
	}
	@GetMapping("/send-mail-verif")
	public User GenerateMailCode(@Param("email")  String email) throws Exception {
		return userService.sendConfMail(email);
	}
}
