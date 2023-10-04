package com.findme.api.service;

import com.findme.api.mapper.PostMapper;
import com.findme.api.model.Post;
import com.findme.api.model.dto.PostDTO;
import com.findme.api.repository.PostRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnit4.class)
class PostServiceTest {
	
	@InjectMocks
	PostService postService;
	
	PostRepository postRepository;
	
	@Mock
	PostMapper postMapper;
	
	@Mock
	PostDTO postDTO;
	
	@BeforeEach
	public void setUp() {
		postService = new PostService(postRepository);
		postRepository = Mockito.mock(PostRepository.class);
		Mockito.when(postRepository.save(Mockito.any(Post.class))).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				if (args != null && args.length > 0 && args[0] != null) {
					return args[0];
				}
				return null;
			}
		});
		Mockito.when(postRepository.findById("1")).thenReturn(Optional.of(Mockito.mock(Post.class)));
		Mockito.when(postRepository.findAll()).thenReturn(List.of(Mockito.mock(Post.class)));
		
		postService.postRepository = postRepository;
		postMapper = new PostMapper();
		Mockito.when(postRepository.save(Mockito.mock(Post.class))).thenReturn(Mockito.mock(Post.class));
	}
	
	private PostDTO getPostDTO() {
		postDTO = new PostDTO();
		postDTO.setId("1");
		postDTO.setUserId("1");
//		postDTO.setPicture("picture");
		postDTO.setDescription("description");
		
		Post.Geolocation geolocation = new Post.Geolocation();
		geolocation.setPosX(33.4);
		geolocation.setPosY(44.5);
		geolocation.setZip(76100);
		geolocation.setCity("Rouen");
		geolocation.setCountry("France");
		geolocation.setAddress("address");
		postDTO.setGeolocation(geolocation);
		
		Post.View view = new Post.View();
		view.setUserId("1");
		postDTO.setView(view);
		
		Post.Like like = new Post.Like();
		like.setUserId("1");
		postDTO.setLike(like);
		postDTO.setVerified(true);
		
		return postDTO;
	}
	
	@Test
	void createPost() {
		Post newPost = postService.createPost(postMapper.toEntity(getPostDTO()));
		Assert.assertEquals(newPost.getUserId(), getPostDTO().getUserId());
	}
	
	@Test
	void getAllPosts() {
		postService.createPost(postMapper.toEntity(getPostDTO()));
		List<Post> posts = postService.getAllPosts();
		Assert.assertNotNull(posts);
		Assert.assertEquals(posts.size(), 1);
	}
	
	@Test
	void getPostById() {
		Post postById = postService.getPostById("1");
		Assert.assertNotNull(postById);
	}
	
	@Test
	void editPost() {
		Post postById = postService.editPost("1", getPostDTO());
		Assert.assertNotNull(postById);
	}
	
	@Test
	void deletePost() {
		postService.deletePost("1");
		Mockito.verify(postRepository, Mockito.times(1)).deleteById("1");
	}
}