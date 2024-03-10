package com.texhnotest.vk.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//@Table(name = "users")
public class User {

//  @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
//  @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", initialValue = 10)
//  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  private String name;
  private String username;
  private String email;

//  @OneToMany(mappedBy = "user")
//  private List<Post> posts;

  private String phone;
  private String website;

  @Embedded
  private Address address;

  @Embedded
  private Company company;

  @Data
  @Embeddable
  public static class Address {
    private String street;
    private String suite;
    private String city;
    private String zipcode;

    @Embedded
    private Geo geo;
  }

  @Data
  @Embeddable
  public static class Geo {
    private String lat;
    private String lng;
  }

  @Data
  @Embeddable
  public static class Company {
//    @Column(name = "company_name")
    private String name;
    private String catchPhrase;
    private String bs;
  }
}
