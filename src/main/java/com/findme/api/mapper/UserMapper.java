package com.findme.api.mapper;

import com.findme.api.model.User;
import com.findme.api.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public class UserMapper implements MapperInstance<UserDTO, User> {
	@Override
	public UserDTO toDTO(User entity) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(entity.getId());
		userDTO.setUsername(entity.getUsername());
		userDTO.setEmail(entity.getEmail());
		userDTO.setPassword(entity.getPassword());
		userDTO.setAvatar(entity.getAvatar());
		userDTO.setFollowers(entity.getFollowers());
		userDTO.setFollowing(entity.getFollowing());
		userDTO.setRoles(entity.getRoles());
		userDTO.setScore(entity.getScore());
		userDTO.setBiography(entity.getBiography());
		return userDTO;
	}
	
	@Override
	public List<UserDTO> toDTO(List<User> entityList) {
		List<UserDTO> userDTOS = new ArrayList<>();
		entityList.forEach(e -> {
			userDTOS.add(toDTO(e));
		});
		return userDTOS;
	}
	
	@Override
	public User toEntity(UserDTO dto) {
		User user = new User();
		user.setId(dto.getId());
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setAvatar(dto.getAvatar());
		user.setFollowers(dto.getFollowers());
		user.setFollowing(dto.getFollowing());
		user.setRoles(dto.getRoles());
		user.setScore(dto.getScore());
		user.setBiography(dto.getBiography());
		return user;
	}
	
	@Override
	public List<User> toEntity(List<UserDTO> dtoList) {
		List<User> users = new ArrayList<>();
		dtoList.forEach(d -> {
			users.add(toEntity(d));
		});
		return users;
	}
}
