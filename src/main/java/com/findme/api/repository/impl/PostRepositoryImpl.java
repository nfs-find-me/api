package com.findme.api.repository.impl;

import com.findme.api.model.Post;
import com.findme.api.repository.custom.PostRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class PostRepositoryImpl implements PostRepositoryCustom {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Override
    public List<Post> findAllPostsPublishedByFriends(String userId) {
       return null;
    }

    @Override
    public List<Post> findAllMostViewedPosts() {
        Query query = new Query();
        query.addCriteria(Criteria.where("view").exists(true));
        query.with(Sort.by(Sort.Direction.ASC, "view"));
        return mongoTemplate.find(query, Post.class);
    }

    @Override
    public List<Post> findAllMostLikedPosts() {
        Query query = new Query();
        query.addCriteria(Criteria.where("like").exists(true));
        query.with(Sort.by(Sort.Direction.ASC, "like"));
        return mongoTemplate.find(query, Post.class);
    }

    @Override
    public List<Post> findAllMostPopularPosts() {
        Query query = new Query();
        query.addCriteria(Criteria.where("view").exists(true));
        query.addCriteria(Criteria.where("like").exists(true));
        query.with(Sort.by(Sort.Direction.ASC, "view, like"));
        return mongoTemplate.find(query, Post.class);
    }

    @Override
    public List<Post> findAllMostRecentPosts() {
        Query query = new Query();
        query.addCriteria(Criteria.where("createdAt").exists(true));
        query.with(Sort.by(Sort.Direction.ASC, "createdAt"));
        return mongoTemplate.find(query, Post.class);
    }

    @Override
    public List<Post> findAllOldestPosts() {
        Query query = new Query();
        query.addCriteria(Criteria.where("createdAt").exists(true));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return mongoTemplate.find(query, Post.class);
    }
}
