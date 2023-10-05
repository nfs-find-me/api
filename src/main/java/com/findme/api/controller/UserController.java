package com.findme.api.controller;

import com.findme.api.mapper.PostMapper;
import com.findme.api.mapper.UserMapper;
import com.findme.api.model.Post;
import com.findme.api.model.User;
import com.findme.api.model.dto.PostDTO;
import com.findme.api.repository.UserRepository;
import com.findme.api.response.ResponseJson;
import com.findme.api.service.PostService;
import com.findme.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private String currentImage;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private final UserService userService;

	@Autowired
	UserMapper userMapper = new UserMapper();

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/avatar")
	public ResponseJson<String> createPost(@RequestParam("file") MultipartFile file,@Param("user") String user) throws IOException {
		if(user==null || user.isBlank()){
			throw new IOException("No user provided");
		}
		Optional<User> currentUser = userRepository.findById(user);
		if(currentUser.isEmpty()){
			throw new IOException("No user found");
		}
		currentImage = userService.uploadAvatar(file);
		currentUser.get().setAvatar(currentImage);
		System.out.println(currentUser.get());
		userRepository.save(currentUser.get());
		return new ResponseJson<>(currentImage, HttpStatus.OK.value());
	}

}
