package com.informationsecurity.PasteBinService.models;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
}
