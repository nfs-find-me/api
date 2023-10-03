package com.findme.api.service;

import com.findme.api.mapper.UserMapper;
import com.findme.api.model.AuthRequest;
import com.findme.api.model.User;
import com.findme.api.model.dto.UserDTO;
import com.findme.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	UserRepository userRepository;
	
	UserMapper userMapper = new UserMapper();
	
	UserService userService;
	
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.userService = new UserService(userRepository);
	}
	
	public User register(AuthRequest authRequest) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(authRequest.getUsername());
		userDTO.setEmail(authRequest.getEmail());
		userDTO.setPassword(bCryptPasswordEncoder.encode(authRequest.getPassword()));
		
		return userService.createUser(userDTO);
	}
}
