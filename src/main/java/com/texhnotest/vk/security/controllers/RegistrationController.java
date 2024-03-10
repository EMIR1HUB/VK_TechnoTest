package com.texhnotest.vk.security.controllers;

import com.texhnotest.vk.security.models.Role;
import com.texhnotest.vk.security.models.User;
import com.texhnotest.vk.security.services.RoleService;
import com.texhnotest.vk.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RegistrationController {

  @Autowired
  private UserService userService;

  @PostMapping("/registration")
  public ResponseEntity<String> registerUser(@RequestBody User user) {
    // Проверка, существует ли пользователь с таким же именем пользователя
    if (userService.existsByUsername(user.getUsername())) {
      return ResponseEntity.badRequest().body("Username is already taken");
    }
    userService.createUser(user);

    return ResponseEntity.ok("Registration successful");
  }

  @GetMapping("/assign/{userId}/{roleId}")
  public ResponseEntity<String> assignRole(@PathVariable Long userId, @PathVariable Long roleId) {
    userService.assignUserRole(userId, roleId);
    return ResponseEntity.ok("AssignUserRole successful");
  }
}
