package com.example.reddit.mapper;

import com.example.reddit.controller.AuthService;
import com.example.reddit.dto.PostRequest;
import com.example.reddit.dto.PostResponse;
import com.example.reddit.model.Post;
import com.example.reddit.model.Subreddit;
import com.example.reddit.model.User;
import com.example.reddit.repository.CommentRepository;
import com.example.reddit.repository.VoteRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

// @Mapper(componentModel = "spring")
@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    AuthService authService;

    // PostMapper MAPPER = Mappers.getMapper(PostMapper.class);
    @Mappings({
            @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
            @Mapping(target = "subreddit", source = "subreddit"),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "description", source = "postRequest.description"),
            @Mapping(target = "voteCount", constant = "0")
    })
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);


    @Mappings({
            @Mapping(target = "id", source = "post.postId"),
            @Mapping(target = "postName", source = "post.postName"),
            @Mapping(target = "description", source = "post.description"),
            @Mapping(target = "url", source = "post.url"),
            @Mapping(target = "subredditName", source = "post.subreddit.name"),
            @Mapping(target = "userName", source = "post.user.username"),
            @Mapping(target = "commentCount", expression = "java(commentCount(post))"),
            @Mapping(target = "duration", expression = "java(getDuration(post))"),


    })
    public abstract PostResponse mapToDto(Post post);


    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}