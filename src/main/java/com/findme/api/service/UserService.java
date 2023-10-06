package com.findme.api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.findme.api.exception.CustomUnauthorizedException;
import com.findme.api.mapper.UserMapper;
import com.findme.api.model.Role;
import com.findme.api.model.User;
import com.findme.api.model.dto.UserDTO;
import com.findme.api.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	
	public User getUserConnected() {
		UserDetails userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return userRepository.findByUsername(userDetails.getUsername());
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
	
	public User editUser(String id, UserDTO userDTO) throws CustomUnauthorizedException {
		User user = userRepository.findById(id).orElse(null);
		if (user != null) {
			if (!getUserConnected().getRoles().contains(Role.ADMIN) && getUserConnected().getId().equals(user.getId())) {
				throw new CustomUnauthorizedException("You can't edit this post");
			}
			user.setUsername(userDTO.getUsername());
			user.setEmail(userDTO.getEmail());
			user.setPassword(userDTO.getPassword());
			user.setAvatar(userDTO.getAvatar());
			user.setFollowers(userDTO.getFollowers());
			user.setFollowing(userDTO.getFollowing());
			user.setRoles(userDTO.getRoles());
			user.setScore(userDTO.getScore());
			user.setBiography(userDTO.getBiography());
			return userRepository.save(user);
		} else {
			throw new RuntimeException("User not found");
		}
	}
	
	public void deleteUser(String id) throws CustomUnauthorizedException {
		User user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		if (!getUserConnected().getRoles().contains(Role.ADMIN) && getUserConnected().getId().equals(user.getId())) {
			throw new CustomUnauthorizedException("You can't edit this post");
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

	//	FOLLOW UNFOLLOW
		public User followUser(String sender, String recipient) {
		Optional<User> senderUser = userRepository.findById(sender);
		if (senderUser.isEmpty()) {
			throw new RuntimeException("Sender user not found");
		}
		Optional<User> targetUser = userRepository.findById(recipient);
		if (targetUser.isEmpty()) {
			throw new RuntimeException("Target user not found");
		}

		ObjectId targetId=new ObjectId(targetUser.get().getId());
		ObjectId  senderId=new ObjectId(senderUser.get().getId());
		List<ObjectId> newFollowers = targetUser.get().getFollowers();
		List<ObjectId> newFollowing= senderUser.get().getFollowing();

		if (newFollowing.contains(targetId) || newFollowers.contains(senderId)) {
			newFollowing.remove(targetId);
			newFollowers.remove(senderId);
		} else {
			newFollowers.add(senderId);
			newFollowing.add(targetId);
		}

		targetUser.get().setFollowers(newFollowers);
		senderUser.get().setFollowing(newFollowing);
		userRepository.save(targetUser.get());
		userRepository.save(senderUser.get());
		return senderUser.get();
	}
}
