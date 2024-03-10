package com.texhnotest.vk.security.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime timestamp;
  private String username;

  @Column(length = 1000)
  private String roles;
  private String action;

  @Column(columnDefinition = "TEXT")
  private String requestParams;
  private boolean success;
}