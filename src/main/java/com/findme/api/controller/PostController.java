package com.findme.api.controller;

import com.findme.api.exception.CustomAccessDeniedException;
import com.findme.api.exception.CustomUnauthorizedException;
import com.findme.api.mapper.PostMapper;
import com.findme.api.model.Post;
import com.findme.api.model.dto.PostDTO;
import com.findme.api.response.ResponseJson;
import com.findme.api.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
	public ResponseJson<Post> createPost(@RequestBody PostDTO postDTO) throws Exception {
		if (currentImage == null) {
			throw new Exception("Missing image");
		}
		postDTO.setPicture(currentImage);
		currentImage = null;
		return new ResponseJson<>(postService.createPost(postMapper.toEntity(postDTO)), HttpStatus.OK.value());
	}

	@PostMapping("/image")
	public ResponseJson<Map<String,String>> createPost(@RequestParam("file") MultipartFile file) throws IOException {
		currentImage = postService.uploadImage(file);
		return new ResponseJson<>(currentImage, HttpStatus.OK.value());
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping
	public ResponseJson<List<Post>> getAllPosts() {
		return new ResponseJson<>(postService.getAllPosts(), HttpStatus.OK.value());
	}
	
	@GetMapping("/{id}")
	public ResponseJson<Post> getPostById(@PathVariable String id) {
		return new ResponseJson<>(postService.getPostById(id), HttpStatus.OK.value());
	}
	
	@PutMapping("/{id}")
	public ResponseJson<Post> editPost(@PathVariable String id, @RequestBody PostDTO postDTO) throws CustomUnauthorizedException, CustomAccessDeniedException {
		return new ResponseJson<>(postService.editPost(id, postDTO), HttpStatus.OK.value());
	}
	
	@DeleteMapping("/{id}")
	public void deletePost(@PathVariable String id) throws CustomUnauthorizedException {
		postService.deletePost(id);
	}
}
