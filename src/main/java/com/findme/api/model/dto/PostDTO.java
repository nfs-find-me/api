package com.findme.api.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.findme.api.model.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class PostDTO {
	@Id
	private String id;

	@NotNull
	private String userId;
	
	@NotNull
	private Map<String,String> picture;
	
	private String description;

	@NotNull
	private Post.Geolocation geolocation;
	
	private List<Post.View> view;
	
	private List<Post.Like> like;
	
	@JsonIgnore
	private boolean verified;
}
