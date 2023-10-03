package com.findme.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document("user")
public class User {
	@Id
	@GeneratedValue(generator = "uuid")
	private String id;
	
	@NonNull
	private String username;
	
	@NonNull
	@Indexed(unique = true)
	private String email;
	
	@NonNull
	private String password;
	
	private String avatar;
	
	private List<User> friends = new ArrayList<>();
	
	private List<Role> roles = List.of(Role.USER);
	
	private Integer score = 0;
	
	private String biography;
	
	@NonNull
	private LocalDateTime createdAt;
	
	@NonNull
	private LocalDateTime updatedAt;
	
	

	
}
