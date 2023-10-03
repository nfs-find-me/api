package com.findme.api.service;

import com.findme.api.mapper.UserMapper;
import com.findme.api.model.User;
import com.findme.api.model.dto.UserDTO;
import com.findme.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
	
	UserRepository userRepository;
	
	UserMapper userMapper = new UserMapper();
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
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
}
