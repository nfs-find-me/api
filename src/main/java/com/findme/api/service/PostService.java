package com.findme.api.service;

import com.findme.api.model.Post;
import com.findme.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
	private final PostRepository postRepository;
	
	@Autowired
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
	
	public Post createPost(Post post) {
		return postRepository.save(post);
	}
}
