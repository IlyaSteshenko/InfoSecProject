package com.informationsecurity.PasteBinService.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Pastes")
public class Paste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pastetitle")
    private String title;

    @Column(name = "pastetext")
    private String text;

    @Basic
    @Column(name = "expirationtime")
    private LocalDateTime expirationTime;

    @Column(name = "author")
    private String author;

}
