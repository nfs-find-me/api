package com.findme.api.controller;

import com.findme.api.exception.CustomException;
import com.findme.api.mapper.UserMapper;
import com.findme.api.model.User;
import com.findme.api.repository.UserRepository;
import com.findme.api.response.ResponseJson;
import com.findme.api.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	private System.Logger logger = System.getLogger(UserController.class.getName());
	
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

	@PostMapping("/avatar/{userId}")
	public ResponseJson<String> changeAvatar(@RequestParam("file") MultipartFile file, @PathVariable String userId, HttpServletResponse response) throws CustomException, IOException {
		logger.log(System.Logger.Level.INFO, "Changing avatar for user: " + userId);
		if(userId==null || userId.isBlank()){
			throw new CustomException(response, HttpStatus.BAD_REQUEST, "No user provided");
		}
		Optional<User> currentUser = userRepository.findById(userId);
		if(currentUser.isEmpty()){
			throw new CustomException(response, HttpStatus.BAD_REQUEST, "No user found");
		}
		currentImage = userService.uploadAvatar(file);
		currentUser.get().setAvatar(currentImage);
		userRepository.save(currentUser.get());
		return new ResponseJson<>(currentImage, HttpStatus.OK.value());
	}
	
	@GetMapping("/follow")
	public ResponseJson<User> followUser(@RequestParam("sender") String sender, @RequestParam("recipient") String recipient, HttpServletResponse response) throws CustomException, IOException {
		logger.log(System.Logger.Level.INFO, "Following user: " + recipient + " from user: " + sender);
		if(sender==null || recipient==null || sender.isBlank() || recipient.isBlank()){
			throw new CustomException(response, HttpStatus.BAD_REQUEST, "Missing data");
		}
		return new ResponseJson<>(userService.followUser(sender,recipient), HttpStatus.OK.value());
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping
	public ResponseJson<User> createUser(@RequestBody UserDTO userDTO) {
		logger.log(System.Logger.Level.INFO, "Creating user: " + userDTO.getUsername());
		return new ResponseJson<>(userService.createUser(userDTO), HttpStatus.OK.value());
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping
	public ResponseJson<List<User>> getAllUsers() {
		logger.log(System.Logger.Level.INFO, "Getting all users");
		return new ResponseJson<>(userService.getAllUsers(), HttpStatus.OK.value());
	}
	
	@GetMapping("/filterScore")
	public ResponseJson<List<User>> getAllUsersByScore() {
		logger.log(System.Logger.Level.INFO, "Getting all users by score");
		return new ResponseJson<>(userService.getAllUserByScore(), HttpStatus.OK.value());
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("/ban/{id}")
	public ResponseJson<Void> banUser(@PathVariable String id, HttpServletResponse response) throws IOException, CustomException {
		logger.log(System.Logger.Level.WARNING, "Banning user: " + id);
		userService.banUser(id, response);
		return new ResponseJson<>(null, HttpStatus.OK.value());
	}
	
	@GetMapping("/{id}")
	public ResponseJson<User> getUserById(@PathVariable String id) {
		logger.log(System.Logger.Level.INFO, "Getting user by id: " + id);
		return new ResponseJson<>(userService.getUserById(id), HttpStatus.OK.value());
	}
	
	@PutMapping("/{id}")
	public ResponseJson<User> editUser(@PathVariable String id, @RequestBody UserDTO userDTO) throws CustomUnauthorizedException {
		logger.log(System.Logger.Level.INFO, "Editing user: " + id);
		return new ResponseJson<>(userService.editUser(id, userDTO), HttpStatus.OK.value());
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable String id) throws CustomUnauthorizedException {
		logger.log(System.Logger.Level.WARNING, "Deleting user: " + id);
		userService.deleteUser(id);
	}

//	POINTS
	@PostMapping("/give_points/{id}")
	public ResponseJson<Void> givePoints(@PathVariable String id, @RequestBody Integer points) throws IOException, CustomException {
		System.out.println("points data : " + points);
		if (points>0) {
			userService.givePoints(id, points);
		}

		return new ResponseJson<>(null, HttpStatus.OK.value());
	}
}
