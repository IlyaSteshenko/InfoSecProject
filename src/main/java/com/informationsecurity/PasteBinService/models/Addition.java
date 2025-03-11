package com.informationsecurity.PasteBinService.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Updates")
public class Addition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pasteid")
    private int pasteId;

    @Basic
    @Column(name = "additiondate")
    private LocalDateTime additionDate;

    @Column(name = "updatename")
    private String updateName;

    @Column(name = "updatetext")
    private String updateText;

}
