package com.texhnotest.vk.controllers;

import com.texhnotest.vk.models.Comment;
import com.texhnotest.vk.models.Post;
import com.texhnotest.vk.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api")
public class PostsController {

  private final PostService postService;

  @Autowired
  public PostsController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/posts")
  public ResponseEntity<List<Post>> getAll(@RequestParam(required = false) Long userId) {
    List<Post> posts = postService.getAllPosts(userId);

    return Optional.ofNullable(posts)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build()); // Возвращаем результат клиенту
  }

  @GetMapping("/posts/{id}")
  public ResponseEntity<Post> getPost(@PathVariable Long id) {
    String url = API_URL + "/posts/" + id;
    Post post = restTemplate.getForObject(url, Post.class);

    return Optional.ofNullable(post)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/posts/{id}/comments")
  public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long id) {
    String url = String.format("%s/posts/%d/comments", API_URL, id);
    Comment[] comments = restTemplate.getForObject(url, Comment[].class);

    return Optional.ofNullable(comments)
            .map(Arrays::asList)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/comments")
  public ResponseEntity<List<Comment>> getCommentsByPostIdQueryParam(@RequestParam Long postId) {
    String url = API_URL + "/comments?postId={postId}";
    Comment[] comments = restTemplate.getForObject(url, Comment[].class, postId);

    return Optional.ofNullable(comments)
            .map(Arrays::asList)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }


  @PostMapping("/posts")
  public ResponseEntity<Post> createPost(@RequestBody Post post) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Отправляем запрос на сервер для добавления поста
    HttpEntity<Post> requestEntity = new HttpEntity<>(post, headers);
    ResponseEntity<Post> responseEntity = restTemplate.postForEntity(API_URL + "/posts", requestEntity, Post.class);

    return Optional.of(responseEntity)
            .filter(res -> res.getStatusCode() == HttpStatus.CREATED)
            .map(res -> ResponseEntity.ok(res.getBody()))
            .orElse(ResponseEntity.status(responseEntity.getStatusCode()).build());
  }

  @PutMapping("/posts/{id}")
  public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
    String url = API_URL + "/posts/{id}";
    Map<String, Long> params = Collections.singletonMap("id", id);

    ResponseEntity<Post> responseEntity = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            new HttpEntity<>(post),
            Post.class,
            params
    );

    return Optional.ofNullable(responseEntity.getBody())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/posts/{id}")
  public ResponseEntity<String> deletePost(@PathVariable Long id) {
    String url = API_URL + "/posts/{id}";
    Map<String, Long> params = Collections.singletonMap("id", id);
    restTemplate.delete(url, params);

    return ResponseEntity.ok("Post with Id=\" + id + \" has been successfully deleted");
  }

}
