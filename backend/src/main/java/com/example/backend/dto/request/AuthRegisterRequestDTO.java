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


    public AuthRegisterRequestDTO() {
    }

    public AuthRegisterRequestDTO(String name, String lastName, String displayName, String email, String password, String repeatedPassword) {
        this.name = name;
        this.lastName = lastName;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
    }
}


