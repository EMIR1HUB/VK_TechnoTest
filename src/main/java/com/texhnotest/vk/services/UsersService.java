package com.texhnotest.vk.services;

import com.texhnotest.vk.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UsersService {

  private final String API_URL = "https://jsonplaceholder.typicode.com";
  private final RestTemplate restTemplate;
  private final CacheManager cacheManager;

  @Autowired
  public UsersService(RestTemplate restTemplate, CacheManager cacheManager) {
    this.restTemplate = restTemplate;
    this.cacheManager = cacheManager;
  }

  public List<User> getAllUsers() {
    String cacheKey = "allUsers_";
    List<User> cachedUsers = (List<User>) cacheManager.get(cacheKey).orElse(Collections.emptyList());

    if (!cachedUsers.isEmpty()) {
      return cachedUsers;
    }

    String url = API_URL + "/users";
    User[] users = restTemplate.getForObject(url, User[].class);

    List<User> result = Optional.ofNullable(users)
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

    cacheManager.put(cacheKey, result);
    return result;
  }

  public User getUser(Long id) {
    String cacheKey = "User_" + id;
    User cachedUsers = (User) cacheManager.get(cacheKey).orElse(null);
    if (cachedUsers != null) {
      return cachedUsers;
    }

    String url = API_URL + "/users/" + id;
    User user = restTemplate.getForObject(url, User.class);
    if (user != null) {
      cacheManager.put(cacheKey, user);
    }

    return user;
  }

  public List<Post> getPostsByUserId(Long userId) {
    String cacheKey = "PostsByUserId_" + userId;
    List<Post> cachedPosts = (List<Post>) cacheManager.get(cacheKey).orElse(Collections.emptyList());
    if (!cachedPosts.isEmpty()) {
      return cachedPosts;
    }

    String url = API_URL + "/posts?userId={id}";
    Post[] posts = restTemplate.getForObject(url, Post[].class, userId);

    List<Post> result = Optional.ofNullable(posts)
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

    cacheManager.put(cacheKey, result);
    return result;
  }

  public List<Todos> getTodosByUserId(Long userId) {
    String cacheKey = "TodosByUserId_" + userId;
    List<Todos> cachedTodos = (List<Todos>) cacheManager.get(cacheKey).orElse(Collections.emptyList());
    if (!cachedTodos.isEmpty()) {
      return cachedTodos;
    }

    String url = String.format("%s/users/%d/todos", API_URL, userId);
    Todos[] todos = restTemplate.getForObject(url, Todos[].class);

    List<Todos> result = Optional.ofNullable(todos)
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

    cacheManager.put(cacheKey, result);
    return result;
  }

  public List<Album> getAlbumsByUserId(Long userId) {
    String cacheKey = "AlbumsByUserId_" + userId;
    List<Album> cachedAlbums = (List<Album>) cacheManager.get(cacheKey).orElse(Collections.emptyList());
    if (!cachedAlbums.isEmpty()) {
      return cachedAlbums;
    }

    String url = String.format("%s/users/%d/albums", API_URL, userId);
    Album[] albums = restTemplate.getForObject(url, Album[].class);

    List<Album> result = Optional.ofNullable(albums)
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

    cacheManager.put(cacheKey, result);
    return result;
  }

  public ResponseEntity<User> createUser(User user) {
    // Очистите кэш для данного запроса
    cacheManager.evict("allUsers_");

    String url = API_URL + "/users";
    return restTemplate.postForEntity(url, user, User.class);
  }

  public ResponseEntity<User> updateUser(Long id, User user) {
    // Очистите кэш для данного запроса
    cacheManager.evict("allUsers_");

    String url = API_URL + "/users/{id}";
    Map<String, Long> params = Collections.singletonMap("id", id);

    return restTemplate.exchange(
            url,
            HttpMethod.PUT,
            new HttpEntity<>(user),
            User.class,
            params
    );
  }

  public void deleteUser(Long id) {
    // Очистите кэш для данного запроса
    cacheManager.evict("allUsers_");

    // Удалите пост на сервере
    String url = API_URL + "/users/{id}";
    restTemplate.delete(url, id);
  }


}
