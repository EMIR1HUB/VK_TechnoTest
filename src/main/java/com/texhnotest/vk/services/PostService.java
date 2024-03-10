package com.texhnotest.vk.services;

import com.texhnotest.vk.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

  private final String API_URL = "https://jsonplaceholder.typicode.com";
  private final RestTemplate restTemplate;
  private final CacheManager cacheManager;

  @Autowired
  public PostService(RestTemplate restTemplate, CacheManager cacheManager) {
    this.restTemplate = restTemplate;
    this.cacheManager = cacheManager;
  }

  public List<Post> getAllPosts(Long userId) {
    String cacheKey = "allPosts_" + userId;
    List<Post> cachedPosts = (List<Post>) cacheManager.get(cacheKey).orElse(Collections.emptyList());

    if (!cachedPosts.isEmpty()) {
      return cachedPosts;
    }

    String url = userId == null ? API_URL + "/posts" : API_URL + "/posts?userId={userId}";
    Post[] posts = restTemplate.getForObject(url, Post[].class, userId);

    List<Post> result = Optional.ofNullable(posts)
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

    cacheManager.put(cacheKey, result);
    return result;
  }

  public Post getPost (Long id) {
    String cacheKey = "Post_" + id;
    Post cachedPosts = (Post) cacheManager.get(cacheKey).orElse(null);

    if (cachedPosts != null) {
      return cachedPosts;
    }

    String url = API_URL + "/posts/" + id;
    Post post = restTemplate.getForObject(url, Post.class);

    if (post != null) {
      cacheManager.put(cacheKey, post);
    }

    return post;
  }



}
