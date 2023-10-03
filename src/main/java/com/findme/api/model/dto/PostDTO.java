package com.findme.api.model.dto;

import com.findme.api.model.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
@NoArgsConstructor
public class PostDTO {
	@Id
	private String id;
	
	private String userId;
	
	private Map<String,String> picture;
	
	private String description;
	
	private Post.Geolocation geolocation;
	
	private Post.View view;
	
	private Post.Like like;
	
	private boolean verified;
}
