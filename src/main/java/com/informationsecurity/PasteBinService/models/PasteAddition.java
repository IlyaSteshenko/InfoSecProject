package com.informationsecurity.PasteBinService.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "changes")
public class PasteAddition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pasteid")
    private Long pasteId;

    @Column(name = "additiontext")
    private String additionText;

}
