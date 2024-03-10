package com.texhnotest.vk.controllers;

import com.texhnotest.vk.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api")
public class UsersController {
  private final String API_URL = "https://jsonplaceholder.typicode.com";
  private final RestTemplate restTemplate;

  @Autowired
  public UsersController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping("/users")
  public ResponseEntity<List<User>> getAll() {
    String url = API_URL + "/users";
    List<User> users = Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(url, User[].class)));
    return ResponseEntity.ok(users);   // Возвращаем результат клиенту
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
    String url = API_URL + "/users/" + id;
    User user = restTemplate.getForObject(url, User.class);
    return ResponseEntity.ok(user);
  }

  @GetMapping("/users/{id}/posts")
  public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable("id") Long userId) {
    String url = API_URL + "/posts?userId={id}";
    List<Post> posts = List.of(
            Objects.requireNonNull(restTemplate.getForObject(url, Post[].class, userId))
    );
    return ResponseEntity.ok(posts);
  }

  @GetMapping("/users/{id}/todos")
  public ResponseEntity<List<Todos>> getTodosByUserId(@PathVariable("id") Long userId) {
    String url = String.format("%s/users/%d/todos", API_URL, userId);
    List<Todos> todos = List.of(
            Objects.requireNonNull(restTemplate.getForObject(url, Todos[].class))
    );
    return ResponseEntity.ok(todos);
  }

  @GetMapping("/users/{id}/albums")
  public ResponseEntity<List<Album>> getAlbumsByUserId(@PathVariable("id") Long userId) {
    String url = String.format("%s/users/%d/albums", API_URL, userId);
    List<Album> albums = List.of(
            Objects.requireNonNull(restTemplate.getForObject(url, Album[].class))
    );
    return ResponseEntity.ok(albums);
  }

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Отправляем запрос на сервер для добавления поста
    HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
    ResponseEntity<User> responseEntity = restTemplate.postForEntity(API_URL + "/users", requestEntity, User.class);

    if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
      return ResponseEntity.ok(responseEntity.getBody());
    } else {
      return ResponseEntity.status(responseEntity.getStatusCode()).build();
    }
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    String url = API_URL + "/users/{id}";
    Map<String, Long> params = new HashMap<>();
    params.put("id", id);

    ResponseEntity<User> responseEntity = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            new HttpEntity<>(user),
            User.class,
            params
    );
    return ResponseEntity.ok(responseEntity.getBody());
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<String> deletePost(@PathVariable Long id) {
    String url = API_URL + "/users/{id}";
    Map<String, Long> params = new HashMap<>();
    params.put("id", id);
    restTemplate.delete(url, params);
    return ResponseEntity.ok("User with Id=" + id + " has been successfully deleted");
  }

}
