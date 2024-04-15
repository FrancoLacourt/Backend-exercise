package com.example.backend.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthLoginRequestDTO {

    private String email;
    private String password;

    public AuthLoginRequestDTO() {

    }

    public AuthLoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
