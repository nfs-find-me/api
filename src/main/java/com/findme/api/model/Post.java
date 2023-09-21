package com.findme.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document("post")
public class Post {
	@Id
	private String id;
	
	@NonNull
	private String userId;
	
	@NonNull
	private String picture;
	
	private String description;
	
	@NonNull
	private Geolocation geolocation;
	
	private View view;
	
	private Like like;
	
	@NonNull
	private boolean verified;
	
	@NonNull
	private LocalDateTime createdAt;
	
	@NonNull
	private LocalDateTime updatedAt;
	
	@Data
	public static class Geolocation {
		public Float posX;
		
		public Float posY;
		
		public Integer zip;
		
		public String city;
		
		public String country;
		
		public String address;
	}
	
	@Data
	public static class View {
		public String userId;
		
		public LocalDateTime createdAt;
	}
	
	@Data
	private static class Like {
		private String userId;
		
		private LocalDateTime createdAt;
	}
	
}
