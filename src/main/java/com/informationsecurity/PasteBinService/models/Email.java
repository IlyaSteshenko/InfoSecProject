package com.informationsecurity.PasteBinService.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String recipient;
    private String subject;
    private String title;
    private String message;
}
