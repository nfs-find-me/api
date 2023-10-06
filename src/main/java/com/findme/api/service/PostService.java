package com.findme.api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.findme.api.exception.CustomUnauthorizedException;
import com.findme.api.model.Post;
import com.findme.api.model.Role;
import com.findme.api.model.dto.PostDTO;
import com.findme.api.repository.PostRepository;
import com.findme.api.repository.custom.PostRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {
	
	@Autowired
	private UserService userService;
	
	PostRepository postRepository;
	
	PostRepositoryCustom postRepositoryCustom;

	@Autowired
	private Environment environment;
	
	@Autowired
	public PostService(PostRepository postRepository, PostRepositoryCustom postRepositoryCustom) {
		this.postRepository = postRepository;
		this.postRepositoryCustom = postRepositoryCustom;
	}
	
	public Post createPost(Post post) {
		return postRepository.save(post);
	}
	
	public void addView(Post post) {
		List<Post.View> views = post.getView();
		// On vérifie si l'utilisateur a déjà vu le post
		if (views.stream().anyMatch(view -> view.getUserId().equals(userService.getUserConnected().getId()))) {
			return;
		}
		views.add(new Post.View(userService.getUserConnected().getId()));
		postRepository.save(post);
	}
	
	public void toggleLike(String id) {
		Post post = postRepository.findById(id).orElse(null);
		if (post == null) {
			throw new RuntimeException("Post not found");
		}
		List<Post.Like> likes = post.getLike();
		if (likes.stream().anyMatch(like -> like.getUserId().equals(userService.getUserConnected().getId()))) {
			likes.removeIf(like -> like.getUserId().equals(userService.getUserConnected().getId()));
			postRepository.save(post);
		} else {
			likes.add(new Post.Like(userService.getUserConnected().getId()));
			postRepository.save(post);
		}
	}
	
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}
	
	// Filters
	public List<Post> getAllMostViewedPosts() {
		return postRepositoryCustom.findAllMostViewedPosts();
	}
	
	public List<Post> getAllMostLikedPosts() {
		return postRepositoryCustom.findAllMostLikedPosts();
	}
	
	public List<Post> getAllMostPopularPosts() {
		return postRepositoryCustom.findAllMostPopularPosts();
	}
	
	public List<Post> getAllMostRecentPosts() {
		return postRepositoryCustom.findAllMostRecentPosts();
	}
	
	public List<Post> getAllOldestPosts() {
		return postRepositoryCustom.findAllOldestPosts();
	}
	
	
	public Post getPostById(String id) {
		Post post = postRepository.findById(id).orElse(null);
		addView(post);
		return post;
	}
	
	public Post editPost(String id, PostDTO postDTO) throws CustomUnauthorizedException {
		if (!userService.getUserConnected().getRoles().contains(Role.ADMIN) && !userService.getUserConnected().getId().equals(postDTO.getUserId())) {
			throw new CustomUnauthorizedException("You can't edit this post");
		}
		Post post = postRepository.findById(id).orElse(null);
		if (post != null) {
			post.setUserId(postDTO.getUserId());
			post.setPicture(postDTO.getPicture());
			post.setDescription(postDTO.getDescription());
			post.setGeolocation(postDTO.getGeolocation());
			post.setView(List.of(postDTO.getView()));
			post.setLike(List.of(postDTO.getLike()));
			post.setVerified(postDTO.isVerified());
			return postRepository.save(post);
		} else {
			throw new RuntimeException("Post not found");
		}
	}
	
	public void deletePost(String id) throws CustomUnauthorizedException {
		Post post = postRepository.findById(id).orElse(null);
		if (post == null) {
			throw new CustomUnauthorizedException("Post not found");
		}
		if (!userService.getUserConnected().getRoles().contains(Role.ADMIN) && !userService.getUserConnected().getId().equals(post.getId())) {
			throw new CustomUnauthorizedException("You can't edit this post");
		}
		postRepository.deleteById(id);
	}

	public Map<String,String> uploadImage(MultipartFile file) {
		Map params = ObjectUtils.asMap(
				"folder", "find-me/posts/",
				"use_filename", false,
				"unique_filename", true,
				"overwrite", true
		);
		Cloudinary cloudinary = new Cloudinary(environment.getProperty("cloudinary_url"));
		Map upload = null;
		try {
			upload = cloudinary.uploader().upload(file.getBytes(), params);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Map<String,String> data = new HashMap<String,String>();
		data.put("url",(String) upload.get("url"));
		data.put("thumbnail_url", ((String) upload.get("url")).replace("upload/", "upload/h_330,c_scale/"));
		return data;
	}
}
