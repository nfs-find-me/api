package com.findme.api.controller;

import com.findme.api.mapper.PostMapper;
import com.findme.api.model.Post;
import com.findme.api.model.dto.PostDTO;
import com.findme.api.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
	private final PostService postService;
	
	PostMapper postMapper = new PostMapper();
	
	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@PostMapping
	public Post createPost(@RequestBody PostDTO postDTO) {
		return postService.createPost(postMapper.toEntity(postDTO));
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
