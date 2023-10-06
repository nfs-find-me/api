package com.findme.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Document("post")
public class Post {
	@Id
	@GeneratedValue(generator = "uuid")
	private String id;
	
	@NonNull
	private String userId;
	
	private Map<String,String> picture;
	
	private String description;
	
	@NonNull
	private Geolocation geolocation;
	
	private View view;
	
	private List<Like> like;
	
	private boolean verified = false;
	
	private LocalDateTime createdAt = LocalDateTime.now();
	
	private LocalDateTime updatedAt = LocalDateTime.now();
	
	@Data
	public static class Geolocation {
		public Double posX;
		
		public Double posY;
		
		public Integer zip;
		
		public String city;
		
		public String country;
		
		public String address;
	}
	
	@Data
	public static class View {
		public String userId;
		
		public LocalDateTime createdAt = LocalDateTime.now();
	}
	
	@Data
	public static class Like {
		private ObjectId userId;
		
		private LocalDateTime createdAt = LocalDateTime.now();
	}
	
}
