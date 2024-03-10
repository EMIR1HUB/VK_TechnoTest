package com.texhnotest.vk.services;

import com.texhnotest.vk.models.Comment;
import com.texhnotest.vk.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

  public Post getPost(Long id) {
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

  public List<Comment> getCommentsByPostId(Long id) {
    String cacheKey = "CommentsByPostId_" + id;
    List<Comment> cachedComments = (List<Comment>) cacheManager.get(cacheKey).orElse(Collections.emptyList());
    if (!cachedComments.isEmpty()) {
      return cachedComments;
    }

    String url = String.format("%s/posts/%d/comments", API_URL, id);
    Comment[] comments = restTemplate.getForObject(url, Comment[].class);

    List<Comment> result = Optional.ofNullable(comments)
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

    cacheManager.put(cacheKey, result);
    return result;
  }

  public List<Comment> getCommentsByPostIdQueryParam(Long postId) {
    String cacheKey = "CommentsByPostIdQueryParam_" + postId;
    List<Comment> cachedComments = (List<Comment>) cacheManager.get(cacheKey).orElse(Collections.emptyList());
    if (!cachedComments.isEmpty()) {
      return cachedComments;
    }

    String url = API_URL + "/comments?postId={postId}";
    Comment[] comments = restTemplate.getForObject(url, Comment[].class, postId);

    List<Comment> result = Optional.ofNullable(comments)
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

    cacheManager.put(cacheKey, result);
    return result;
  }

  public ResponseEntity<Post> createPost(Post post) {
    // Очистите кэш для данного запроса
    cacheManager.evict("allPosts_");

    String url = API_URL + "/posts";
    return restTemplate.postForEntity(url, post, Post.class);
  }

  public ResponseEntity<Post> updatePost(Long id, Post post) {
    // Очистите кэш для данного запроса
    cacheManager.evict("allPosts_");

    String url = API_URL + "/posts/{id}";
    Map<String, Long> params = Collections.singletonMap("id", id);

    return restTemplate.exchange(
            url,
            HttpMethod.PUT,
            new HttpEntity<>(post),
            Post.class,
            params
    );
  }

  public void deletePost(Long id) {
    // Очистите кэш для данного запроса
    cacheManager.evict("allPosts_");

    // Удалите пост на сервере
    String url = API_URL + "/posts/{id}";
    restTemplate.delete(url, id);
  }
}
