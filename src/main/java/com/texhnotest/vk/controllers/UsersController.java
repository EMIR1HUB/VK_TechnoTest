package com.texhnotest.vk.controllers;

import com.texhnotest.vk.models.*;
import com.texhnotest.vk.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class UsersController {

  private final UsersService userService;

  @Autowired
  public UsersController(UsersService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  public ResponseEntity<List<User>> getAll() {
    List<User> users = userService.getAllUsers();

    return Optional.ofNullable(users)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build()); // Возвращаем результат клиенту
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.getUser(id);

    return Optional.ofNullable(user)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/users/{id}/posts")
  public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable("id") Long userId) {
    List<Post> posts = userService.getPostsByUserId(userId);

    return Optional.ofNullable(posts)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/users/{id}/todos")
  public ResponseEntity<List<Todos>> getTodosByUserId(@PathVariable("id") Long userId) {
    List<Todos> todos = userService.getTodosByUserId(userId);

    return Optional.ofNullable(todos)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/users/{id}/albums")
  public ResponseEntity<List<Album>> getAlbumsByUserId(@PathVariable("id") Long userId) {
    List<Album> albums = userService.getAlbumsByUserId(userId);

    return Optional.ofNullable(albums)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    ResponseEntity<User> responseEntity = userService.createUser(user);

    return Optional.of(responseEntity)
            .filter(res -> res.getStatusCode() == HttpStatus.CREATED)
            .map(res -> ResponseEntity.ok(res.getBody()))
            .orElse(ResponseEntity.status(responseEntity.getStatusCode()).build());
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    ResponseEntity<User> responseEntity = userService.updateUser(id, user);

    return Optional.ofNullable(responseEntity.getBody())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<String> updateUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.ok("User with Id=" + id + " has been successfully deleted");
  }

}
