package com.findme.api.repository.custom;

import com.findme.api.model.Post;
import com.findme.api.repository.PostRepository;

import java.util.List;

public interface PostRepositoryCustom {
    
    List<Post> findAllPostsPublishedByFriends(String userId);
    
    List<Post> findAllMostViewedPosts();
    
    List<Post> findAllMostLikedPosts();
    
    List<Post> findAllMostPopularPosts();
    
    List<Post> findAllMostRecentPosts();
    
    List<Post> findAllOldestPosts();
}
