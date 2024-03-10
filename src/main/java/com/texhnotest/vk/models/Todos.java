package com.texhnotest.vk.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Todos {
//  @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_seq_generator")
//  @SequenceGenerator(name = "todo_seq_generator", sequenceName = "todo_seq", initialValue = 10)
//  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  private String title;
  private String completed;

//  @ManyToOne
//  @JoinColumn(name = "userId", insertable = false, updatable = false)
//  @JsonIgnore
//  private User user;
  private Long userId;
}
