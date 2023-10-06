package com.findme.api.model.dto;

import com.findme.api.model.Role;
import com.findme.api.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDTO {
	@Id
	private String id;
	
	@NonNull
	@Indexed(unique = true)
	private String username;
	
	@NonNull
	@Indexed(unique = true)
	private String email;
	
	@NonNull
	private String password;
	
	private String avatar;

	private String confirmationCode;

	private List<ObjectId> followers = new ArrayList<>();

	private List<ObjectId> following = new ArrayList<>();
	
	private List<Role> roles = List.of(Role.USER);
	
	private Integer score = 0;
	
	private String biography;
}
