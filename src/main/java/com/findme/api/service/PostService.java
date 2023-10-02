package com.findme.api.service;

import com.findme.api.model.Post;
import com.findme.api.model.dto.PostDTO;
import com.findme.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
	PostRepository postRepository;
	
	@Autowired
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
	
	public Post createPost(Post post) {
		return postRepository.save(post);
	}
	
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}
	
	public Post getPostById(String id) {
		return postRepository.findById(id).orElse(null);
	}
	
	public Post editPost(String id, PostDTO postDTO) {
		Post post = postRepository.findById(id).orElse(null);
		if (post != null) {
			post.setUserId(postDTO.getUserId());
			post.setPicture(postDTO.getPicture());
			post.setDescription(postDTO.getDescription());
			post.setGeolocation(postDTO.getGeolocation());
			post.setView(postDTO.getView());
			post.setLike(postDTO.getLike());
			post.setVerified(postDTO.isVerified());
			return postRepository.save(post);
		} else {
			throw new RuntimeException("Post not found");
		}
	}
	
	public void deletePost(String id) {
		Post post = postRepository.findById(id).orElse(null);
		if (post == null) {
			throw new RuntimeException("Post not found");
		}
		postRepository.deleteById(id);
	}
}
