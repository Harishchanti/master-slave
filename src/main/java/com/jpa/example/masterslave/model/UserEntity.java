package com.jpa.example.masterslave.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "user")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @JsonProperty
    private String name;

    @Column(name = "age")
    @JsonProperty
    private Long age;

    @Column(name = "gender")
    @JsonProperty
    private String gender;

}