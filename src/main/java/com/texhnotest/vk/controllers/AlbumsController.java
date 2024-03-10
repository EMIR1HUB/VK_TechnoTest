package com.texhnotest.vk.controllers;

import com.texhnotest.vk.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api")
public class AlbumsController {
  private final String API_URL = "https://jsonplaceholder.typicode.com";
  private final RestTemplate restTemplate;

  @Autowired
  public AlbumsController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping("/albums")
  public ResponseEntity<List<Album>> getAll(@RequestParam(required = false) Long userId) {
    String url = userId == null ? API_URL + "/albums" : API_URL + "/albums?userId={userId}";
    Album[] albums = restTemplate.getForObject(url, Album[].class, userId);

    return Optional.ofNullable(albums)
            .map(Arrays::asList)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/albums/{id}")
  public ResponseEntity<Album> getAlbum(@PathVariable Long id) {
    String url = API_URL + "/albums/" + id;
    Album album = restTemplate.getForObject(url, Album.class);

    return Optional.ofNullable(album)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/albums/{id}/photos")
  public ResponseEntity<List<Photos>> getPhotosByAlbumId(@PathVariable("id") Long albumId) {
    String url = String.format("%s/albums/%d/photos", API_URL, albumId);
    Photos[] photos = restTemplate.getForObject(url, Photos[].class);

    return Optional.ofNullable(photos)
            .map(List::of)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/albums")
  public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Отправляем запрос на сервер для добавления поста
    HttpEntity<Album> requestEntity = new HttpEntity<>(album, headers);
    ResponseEntity<Album> responseEntity = restTemplate.postForEntity(API_URL + "/albums", requestEntity, Album.class);

    return Optional.of(responseEntity)
            .filter(res -> res.getStatusCode()==HttpStatus.CREATED)
            .map(res -> ResponseEntity.ok(res.getBody()))
            .orElse(ResponseEntity.status(responseEntity.getStatusCode()).build());
  }

  @PutMapping("/albums/{id}")
  public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @RequestBody Album album) {
    String url = API_URL + "/albums/{id}";
    Map<String, Long> params = Collections.singletonMap("id", id);

    ResponseEntity<Album> responseEntity = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            new HttpEntity<>(album),
            Album.class,
            params
    );

    return Optional.ofNullable(responseEntity.getBody())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/albums/{id}")
  public ResponseEntity<String> deleteAlbum(@PathVariable Long id) {
    String url = API_URL + "/albums/{id}";
    Map<String, Long> params = Collections.singletonMap("id", id);
    restTemplate.delete(url, params);

    return ResponseEntity.ok("Album with Id=" + id + " has been successfully deleted");
  }

}
