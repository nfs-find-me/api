package com.findme.api.controller;

import com.findme.api.model.Post;
import com.findme.api.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/post")
public class PostController {
	private final PostService postService;
	
	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@PostMapping
	public Post createPost() {
		Post post = new Post();
		post.setId("1");
		post.setUserId("userId");
		post.setPicture("picture");
		Post.Geolocation geolocation = new Post.Geolocation();
		geolocation.setPosX("posX");
		geolocation.setPosY("posY");
		geolocation.setZip("zip");
		geolocation.setCity("city");
		geolocation.setCountry("country");
		post.setGeolocation(geolocation);
		post.setCreatedAt(LocalDateTime.now());
		post.setUpdatedAt(LocalDateTime.now());
		return postService.createPost(post);
	}
}
