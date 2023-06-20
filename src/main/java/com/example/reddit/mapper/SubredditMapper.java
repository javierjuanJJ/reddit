package com.example.reddit.mapper;

import com.example.reddit.dto.SubredditDto;
import com.example.reddit.model.Post;
import com.example.reddit.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
    @Mappings({
            @Mapping(target = "subreddit.numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    })
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "subreddit.posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subreddit);
}