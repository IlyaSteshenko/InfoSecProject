package com.informationsecurity.PasteBinService.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "userpassword")
    private String password;

    @Column(name = "useremail")
    private String email;

    @Column(name = "userrole")
    @Enumerated(EnumType.STRING)
    private Role role;

}
