package com.texhnotest.vk.controllers;

import com.texhnotest.vk.models.*;
import com.texhnotest.vk.services.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api")
public class AlbumsController {
  private final AlbumService albumService;

  @Autowired
  public AlbumsController(AlbumService albumService) {
    this.albumService = albumService;
  }

  @GetMapping("/albums")
  public ResponseEntity<List<Album>> getAll(@RequestParam(required = false) Long userId) {
    List<Album> albums = albumService.getAllAlbums(userId);

    return Optional.ofNullable(albums)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/albums/{id}")
  public ResponseEntity<Album> getAlbum(@PathVariable Long id) {
    Album album = albumService.getAlbum(id);

    return Optional.ofNullable(album)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/albums/{id}/photos")
  public ResponseEntity<List<Photos>> getPhotosByAlbumId(@PathVariable("id") Long albumId) {
    List<Photos> photos = albumService.getPhotosByAlbumId(albumId);

    return Optional.ofNullable(photos)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/albums")
  public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
    ResponseEntity<Album> responseEntity = albumService.createAlbum(album);

    return Optional.of(responseEntity)
            .filter(res -> res.getStatusCode()==HttpStatus.CREATED)
            .map(res -> ResponseEntity.ok(res.getBody()))
            .orElse(ResponseEntity.status(responseEntity.getStatusCode()).build());
  }

  @PutMapping("/albums/{id}")
  public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @RequestBody Album album) {
    ResponseEntity<Album> responseEntity = albumService.updateAlbum(id, album);

    return Optional.ofNullable(responseEntity.getBody())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/albums/{id}")
  public ResponseEntity<String> deleteAlbum(@PathVariable Long id) {
    albumService.deleteAlbum(id);

    return ResponseEntity.ok("Album with Id=" + id + " has been successfully deleted");
  }

}
