package com.example.securityjwt.dto;

import lombok.Getter;

@Getter
public class LoginDto {
    private String identity;
    private String password;
}
