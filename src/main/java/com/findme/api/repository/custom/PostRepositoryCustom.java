package com.findme.api.repository.custom;

import com.findme.api.model.Post;

import java.util.List;

public interface PostRepositoryCustom {
    
    List<Post> findAllMostViewedPosts();
    
    List<Post> findAllMostLikedPosts();
    
    List<Post> findAllMostPopularPosts();
    
    List<Post> findAllMostRecentPosts();
    
    List<Post> findAllOldestPosts();
}
