package com.findme.api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.findme.api.mapper.UserMapper;
import com.findme.api.model.User;
import com.findme.api.model.dto.UserDTO;
import com.findme.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
	
	UserRepository userRepository;
	
	UserMapper userMapper = new UserMapper();
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Autowired
	private Environment environment;
	
	public User createUser(UserDTO userDTO) {
		return userRepository.save(userMapper.toEntity(userDTO));
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public User getUserById(String id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public User editUser(String id, UserDTO userDTO) {
		User user = userRepository.findById(id).orElse(null);
		if (user != null) {
			user.setUsername(userDTO.getUsername());
			user.setEmail(userDTO.getEmail());
			user.setPassword(userDTO.getPassword());
			user.setAvatar(userDTO.getAvatar());
			user.setFriends(userDTO.getFriends());
			user.setRoles(userDTO.getRoles());
			user.setScore(userDTO.getScore());
			user.setBiography(userDTO.getBiography());
			return userRepository.save(user);
		} else {
			throw new RuntimeException("User not found");
		}
	}
	
	public void deleteUser(String id) {
		User user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		userRepository.deleteById(id);
	}


//	AVATAR IMAGE
	public String uploadAvatar(MultipartFile file) {
		Map params = ObjectUtils.asMap(
				"folder", "find-me/avatars/",
				"use_filename", false,
				"unique_filename", true,
				"overwrite", true
		);

		Cloudinary cloudinary = new Cloudinary(environment.getProperty("cloudinary_url"));
		Map upload = null;
		try {
			upload = cloudinary.uploader().upload(file.getBytes(), params);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println(((String) upload.get("url")).replace("upload/", "upload/h_330,c_scale/"));
		return ((String) upload.get("url")).replace("upload/", "upload/h_330,c_scale/");
	}
}
