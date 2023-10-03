package com.findme.api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.findme.api.model.Post;
import com.findme.api.model.dto.PostDTO;
import com.findme.api.repository.PostRepository;
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
	PostRepository postRepository;

	@Autowired
	private Environment environment;
	
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

	public Map<String,String> uploadImage(MultipartFile file) {
		System.out.print("file : ");
		System.out.println(file);
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
