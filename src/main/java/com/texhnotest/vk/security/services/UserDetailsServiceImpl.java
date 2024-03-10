package com.texhnotest.vk.security.services;

import com.texhnotest.vk.security.models.User;
import com.texhnotest.vk.security.models.UserPrincipal;
import com.texhnotest.vk.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    return user.map(UserPrincipal::new)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
  }
}