package com.findme.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
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
	@Indexed(unique = true)
	private String username;
	
	@NonNull
	@Indexed(unique = true)
	private String email;
	
	@NonNull
	@JsonIgnore
	private String password;
	
	private String avatar;

	private String confirmationCode;

	private List<ObjectId> followers = new ArrayList<>();

	private List<ObjectId> following = new ArrayList<>();

	private List<Role> roles = List.of(Role.USER);
	
	private Integer score = 0;
	
	private String biography;
	
	@NonNull
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@NonNull
	private LocalDateTime updatedAt ;
	
	

	
}
