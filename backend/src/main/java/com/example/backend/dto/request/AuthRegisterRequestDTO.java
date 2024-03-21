package com.example.backend.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRegisterRequestDTO {

    private String name;
    private String lastName;
    private String displayName;
    private String email;
    private String password;
    private String repeatedPassword;
}
