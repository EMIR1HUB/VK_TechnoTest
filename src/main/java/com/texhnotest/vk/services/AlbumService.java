package com.texhnotest.vk.services;

import com.texhnotest.vk.models.Album;
import com.texhnotest.vk.models.Comment;
import com.texhnotest.vk.models.Photos;
import com.texhnotest.vk.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AlbumService {

  private final String API_URL = "https://jsonplaceholder.typicode.com";
  private final RestTemplate restTemplate;
  private final CacheManager cacheManager;

  @Autowired
  public AlbumService(RestTemplate restTemplate, CacheManager cacheManager) {
    this.restTemplate = restTemplate;
    this.cacheManager = cacheManager;
  }

  public List<Album> getAllAlbums(Long userId) {
    String cacheKey = "allAlbums_" + userId;
    List<Album> cachedAlbums = (List<Album>) cacheManager.get(cacheKey).orElse(Collections.emptyList());

    if (!cachedAlbums.isEmpty()) {
      return cachedAlbums;
    }

    String url = userId == null ? API_URL + "/albums" : API_URL + "/albums?userId={userId}";
    Album[] albums = restTemplate.getForObject(url, Album[].class, userId);

    List<Album> result = Optional.ofNullable(albums)
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

    cacheManager.put(cacheKey, result);
    return result;
  }

  public Album getAlbum(Long id) {
    String cacheKey = "Album_" + id;
    Album cachedAlbums = (Album) cacheManager.get(cacheKey).orElse(null);
    if (cachedAlbums != null) {
      return cachedAlbums;
    }

    String url = API_URL + "/albums/" + id;
    Album album = restTemplate.getForObject(url, Album.class);
    if (album != null) {
      cacheManager.put(cacheKey, album);
    }
    return album;
  }

  public List<Photos> getPhotosByAlbumId(Long albumId) {
    String cacheKey = "PhotosByAlbumId_" + albumId;
    List<Photos> cachedPhotos = (List<Photos>) cacheManager.get(cacheKey).orElse(Collections.emptyList());
    if (!cachedPhotos.isEmpty()) {
      return cachedPhotos;
    }

    String url = String.format("%s/albums/%d/photos", API_URL, albumId);
    Photos[] photos = restTemplate.getForObject(url, Photos[].class);

    List<Photos> result = Optional.ofNullable(photos)
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

    cacheManager.put(cacheKey, result);
    return result;
  }

  public ResponseEntity<Album> createAlbum(Album album) {
    // Очистите кэш для данного запроса
    cacheManager.evict("allAlbums_");

    String url = API_URL + "/albums";
    return restTemplate.postForEntity(url, album, Album.class);
  }

  public ResponseEntity<Album> updateAlbum(Long id, Album album) {
    cacheManager.evict("allAlbums_");

    String url = API_URL + "/albums/{id}";
    Map<String, Long> params = Collections.singletonMap("id", id);

    return restTemplate.exchange(
            url,
            HttpMethod.PUT,
            new HttpEntity<>(album),
            Album.class,
            params
    );
  }

  public void deleteAlbum(Long id) {
    cacheManager.evict("allAlbums_");

    // Удалите пост на сервере
    String url = API_URL + "/albums/{id}";
    restTemplate.delete(url, id);
  }
}
