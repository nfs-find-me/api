package com.findme.api.mapper;

import com.findme.api.model.Post;
import com.findme.api.model.dto.PostDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public class PostMapper implements MapperInstance<PostDTO, Post> {
	
	@Override
	public PostDTO toDTO(Post entity) {
		PostDTO postDTO = new PostDTO();
		postDTO.setId(entity.getId());
		postDTO.setUserId(entity.getUserId());
		postDTO.setPicture(entity.getPicture());
		postDTO.setDescription(entity.getDescription());
		postDTO.setGeolocation(entity.getGeolocation());
		postDTO.setView(entity.getView());
		postDTO.setLike(entity.getLike());
		postDTO.setVerified(entity.isVerified());
		return postDTO;
	}
	
	@Override
	public List<PostDTO> toDTO(List<Post> entityList) {
		List<PostDTO> postDTOS = new ArrayList<>();
		entityList.forEach(e -> {
			postDTOS.add(toDTO(e));
		});
		return postDTOS;
	}
	
	@Override
	public Post toEntity(PostDTO dto) {
		Post post = new Post();
		post.setId(dto.getId());
		post.setUserId(dto.getUserId());
		post.setPicture(dto.getPicture());
		post.setDescription(dto.getDescription());
		post.setGeolocation(dto.getGeolocation());
		post.setView(dto.getView());
		post.setLike(dto.getLike());
		post.setVerified(dto.isVerified());
		return post;
	}
	
	@Override
	public List<Post> toEntity(List<PostDTO> dtoList) {
		List<Post> posts = new ArrayList<>();
		dtoList.forEach(d -> {
			posts.add(toEntity(d));
		});
		return posts;
	}
}
