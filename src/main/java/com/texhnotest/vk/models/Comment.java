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
//@Table(name = "comment")
public class Comment {

//  @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq_generator")
//  @SequenceGenerator(name = "comment_seq_generator", sequenceName = "comment_seq", initialValue = 10)
//  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  private String name;
  private String email;
  private String body;

//  @ManyToOne
//  @JoinColumn(name = "postId", insertable = false, updatable = false)
//  @JsonIgnore
//  private Post post;
  private Long postId;
}
