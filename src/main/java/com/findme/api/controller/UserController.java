package com.findme.api.controller;

import com.findme.api.mapper.UserMapper;
import com.findme.api.model.User;
import com.findme.api.repository.UserRepository;
import com.findme.api.response.ResponseJson;
import com.findme.api.service.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import com.findme.api.exception.CustomUnauthorizedException;
import com.findme.api.model.dto.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;

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
	public ResponseJson<String> changeAvatar(@RequestParam("file") MultipartFile file,@Param("user") String user) throws IOException {
		if(user==null || user.isBlank()){
			throw new IOException("No user provided");
		}
		Optional<User> currentUser = userRepository.findById(user);
		if(currentUser.isEmpty()){
			throw new IOException("No user found");
		}
		currentImage = userService.uploadAvatar(file);
		currentUser.get().setAvatar(currentImage);
		userRepository.save(currentUser.get());
		return new ResponseJson<>(currentImage, HttpStatus.OK.value());
	}

	public static class FollowArgs {
		private String sender;
		private String recipient;

		public FollowArgs(String sender, String recipient){
			this.sender = sender;
			this.recipient = recipient;
		}
	}
	@GetMapping("/follow")
	public ResponseJson<User> followUser(@RequestParam("sender") String sender, @RequestParam("recipient") String recipient) throws IOException {
		System.out.println(sender);
		if(sender==null || recipient==null || sender.isBlank() || recipient.isBlank()){
			throw new IOException("Missing data");
		}
		return new ResponseJson<>(userService.followUser(sender,recipient), HttpStatus.OK.value());
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping
	public ResponseJson<User> createUser(@RequestBody UserDTO userDTO) {
		return new ResponseJson<>(userService.createUser(userDTO), HttpStatus.OK.value());
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping
	public ResponseJson<List<User>> getAllUsers() {
		return new ResponseJson<>(userService.getAllUsers(), HttpStatus.OK.value());
	}
	
	@GetMapping("/{id}")
	public ResponseJson<User> getUserById(@PathVariable String id) {
		return new ResponseJson<>(userService.getUserById(id), HttpStatus.OK.value());
	}
	
	@PutMapping("/{id}")
	public ResponseJson<User> editUser(@PathVariable String id, @RequestBody UserDTO userDTO) throws CustomUnauthorizedException {
		return new ResponseJson<>(userService.editUser(id, userDTO), HttpStatus.OK.value());
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable String id) throws CustomUnauthorizedException {
		userService.deleteUser(id);
	}
}
