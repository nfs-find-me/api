package com.findme.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
	
	private List<View> view = new ArrayList<>();
	
	private List<Like> like = new ArrayList<>();
	
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
		
		public View(String userId) {
			this.userId = userId;
		}
		
		public String userId;
		
		public LocalDateTime createdAt = LocalDateTime.now();
	}
	
	@Data
	public static class Like {
		
		public Like(String userId) {
			this.userId = userId;
		}
		
		private String userId;
		
		private LocalDateTime createdAt = LocalDateTime.now();
	}
	
}
