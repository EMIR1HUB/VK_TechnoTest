package com.texhnotest.vk;

import com.texhnotest.vk.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

    return http.csrf(AbstractHttpConfigurer::disable) // Отключаем CSRF для простоты
            .authorizeHttpRequests(antManReqMatchers -> antManReqMatchers
                    .requestMatchers("/api/registration", "/api/assign/**").permitAll()
                    .requestMatchers("/api/posts/**").hasAnyAuthority("ROLE_POSTS", "ROLE_ADMIN")
                    .requestMatchers("/api/users/**").hasAnyAuthority("ROLE_USERS", "ROLE_ADMIN")
                    .requestMatchers("/api/albums/**").hasAnyAuthority("ROLE_ALBUMS", "ROLE_ADMIN")
                    .anyRequest().authenticated())
            .authenticationProvider(authenticationProvider())
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
            .build();

  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // AuthenticationProvider используется для подтверждения личности пользователя (кто ты?), возвращает провайдера
  @Bean
  public AuthenticationProvider authenticationProvider() {
    // реализация провайдера, который реализует userDetailsService и passwordEncoder
    // для аутентификации имя пользователя и пароля
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
