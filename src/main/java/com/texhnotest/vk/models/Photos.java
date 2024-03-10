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
public class Photos {

//  @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_seq_generator")
//  @SequenceGenerator(name = "photo_seq_generator", sequenceName = "photo_seq", initialValue = 10)
//  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  private String title;
  private String url;
  private String thumbnailUrl;

//  @ManyToOne
//  @JoinColumn(name = "albumId", insertable = false, updatable = false)
//  @JsonIgnore
//  private Album user;
  private Long albumId;
}
