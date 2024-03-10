package com.texhnotest.vk.security.services;


import com.texhnotest.vk.security.models.User;
import com.texhnotest.vk.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
  @Autowired
  private UserRepository userRepository;

}
