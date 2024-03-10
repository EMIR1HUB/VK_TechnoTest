package com.texhnotest.vk.security.services;


import com.texhnotest.vk.security.models.User;
import com.texhnotest.vk.security.repositories.RoleRepository;
import com.texhnotest.vk.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Service
public class UserService {
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  public void createUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
  }

  public boolean existsByUsername(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  public void assignUserRole(Long userId, Long roleId) {
    userRepository.findById(userId).ifPresentOrElse(user -> {
      roleRepository.findById(roleId).ifPresent(role -> {
        user.getRoles().add(role);
        userRepository.save(user);
      });
    }, () -> {
      throw new RuntimeException("Пользователь с id " + userId + " не найден");
    });
  }
}
