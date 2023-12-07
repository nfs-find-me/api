package com.findme.api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.findme.api.exception.CustomUnauthorizedException;
import com.findme.api.exception.CustomException;
import com.findme.api.model.Post;
import com.findme.api.model.User;
import com.findme.api.model.Role;
import com.findme.api.model.dto.PostDTO;
import com.findme.api.repository.PostRepository;
import com.findme.api.repository.UserRepository;
import com.findme.api.repository.custom.PostRepositoryCustom;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {
	
	@Autowired
	private UserService userService;
	
	PostRepository postRepository;
	UserRepository userRepository;

	@Autowired
	private AIService aiService;
	
	PostRepositoryCustom postRepositoryCustom;

	@Autowired
	private Environment environment;
	
	@Autowired
	public PostService(PostRepository postRepository, PostRepositoryCustom postRepositoryCustom) {
		this.postRepository = postRepository;
		this.postRepositoryCustom = postRepositoryCustom;
	}
	
	public Post createPost(HttpServletResponse httpServletResponse, MultipartFile file, Post post) throws IOException, CustomException {
		post.setPicture(uploadImage(httpServletResponse, file));
		post.setUser(userService.getUserById(post.getUserId()));
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
		List<Post> posts = postRepository.findAll();
		Collections.shuffle(posts);
		return posts;
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
	
	public List<Post> getAllPostsByFollowing(String userId) {
		User user = userService.getUserById(userId);
		return postRepositoryCustom.findAllFollowingsPosts(userId);
	}
	
	
	public Post getPostById(String id) {
		Post post = postRepository.findById(id).orElse(null);
		addView(post);
		User user = userService.getUserById(post.getUserId());
		post.setUser(user);
		return post;
	}
	
	public List<Post> getPostsByUserId(String id) {
		return postRepository.findAllByUserId(id);
	}
	
	public List<Post> getPostsByUsername(String username, HttpServletResponse response) throws IOException, CustomException {
		User user = userService.getUserByUsername(username);
		if (user == null) {
			return null;
		}
		List<Post> posts = postRepository.findAllByUserId(user.getId());
		System.out.println(posts.stream().count());
		return posts;
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
			post.setView(postDTO.getView());
			post.setLike(postDTO.getLike());
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

	public Map<String,String> uploadImage(HttpServletResponse httpServletResponse, MultipartFile file) throws IOException, CustomException {
		Map params = ObjectUtils.asMap(
				"folder", "find-me/posts/",
				"use_filename", false,
				"unique_filename", true,
				"overwrite", true
		);
		// TODO : Faire la vérification de l'image auprès de l'API avec l'AI
		// Call externe api
 		CloseableHttpResponse response = aiService.aiResponse(file, httpServletResponse);
		
		Map<String,String> data = new HashMap<String,String>();
		if (response.getStatusLine().getStatusCode() == 200) {
			Cloudinary cloudinary = new Cloudinary(environment.getProperty("cloudinary_url"));
			Map upload = null;
			try {
				upload = cloudinary.uploader().upload(file.getBytes(), params);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			data.put("url",(String) upload.get("url"));
			data.put("thumbnail_url", ((String) upload.get("url")).replace("upload/", "upload/h_330,c_scale/"));
		} else {
			throw new RuntimeException("Image not uploaded");
		}
		return data;
	}
}
