package com.example.reddit.mapper;

import com.example.reddit.dto.PostRequest;
import com.example.reddit.dto.PostResponse;
import com.example.reddit.model.Post;
import com.example.reddit.model.Subreddit;
import com.example.reddit.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.repository.JpaRepository;

// @Mapper(componentModel = "spring")
@Mapper(componentModel = "spring")
public interface PostMapper {
    // PostMapper MAPPER = Mappers.getMapper(PostMapper.class);
    @Mappings({
            @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
            @Mapping(target = "subreddit", source = "subreddit"),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "description", source = "postRequest.description"),
    })
    Post map(PostRequest postRequest, Subreddit subreddit, User user);


    @Mappings({
            @Mapping(target = "id", source = "post.postId"),
            @Mapping(target = "postName", source = "post.postName"),
            @Mapping(target = "description", source = "post.description"),
            @Mapping(target = "url", source = "post.url"),
            @Mapping(target = "subredditName", source = "post.subreddit.name"),
            @Mapping(target = "userName", source = "post.user.username"),


    })
    PostResponse mapToDto(Post post);
}