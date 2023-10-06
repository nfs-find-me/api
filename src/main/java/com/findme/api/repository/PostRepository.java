package com.findme.api.repository;

import com.findme.api.model.Post;
import com.findme.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    
    List<Post> findAllByUserId(String userId);
}
