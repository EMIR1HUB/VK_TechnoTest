package com.texhnotest.vk.security.controllers;

import com.texhnotest.vk.security.models.User;
import com.texhnotest.vk.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/create")
  public ResponseEntity<String> createUser(@RequestBody User user) {
    userService.createUser(user);
    return ResponseEntity.ok("User created successfully!!");
  }
}
