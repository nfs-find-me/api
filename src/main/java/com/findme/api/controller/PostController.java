package com.findme.api.controller;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import com.findme.api.mapper.PostMapper;
import com.findme.api.model.Post;
import com.findme.api.model.dto.PostDTO;
import com.findme.api.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class PostController {
	private Map<String,String> currentImage;
	private final PostService postService;
	
	PostMapper postMapper = new PostMapper();
	
	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@PostMapping
	public Post createPost(@RequestBody PostDTO postDTO) {
//		Map<String,String> image = postService.uploadImage(file);
		System.out.println("postDTO");
		System.out.println(postDTO);
		postDTO.setPicture(currentImage);
		return postService.createPost(postMapper.toEntity(postDTO));
	}

	@PostMapping("/image")
	public Map<String,String> createPost(@RequestParam("file") MultipartFile file) throws IOException {
		currentImage = postService.uploadImage(file);
		return currentImage;
	}
	
	@GetMapping
	public List<Post> getAllPosts() {
		return postService.getAllPosts();
	}
	
	@GetMapping("/{id}")
	public Post getPostById(@PathVariable String id) {
		return postService.getPostById(id);
	}
	
	@PutMapping("/{id}")
	public Post editPost(@PathVariable String id, @RequestBody PostDTO postDTO) {
		return postService.editPost(id, postDTO);
	}
	
	@DeleteMapping("/{id}")
	public void deletePost(@PathVariable String id) {
		postService.deletePost(id);
	}
}
