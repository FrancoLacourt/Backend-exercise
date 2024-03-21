package com.example.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AuthResponseDTO {

    private String token;
    private String email;
    private long id;
    private String name;
    private String lastName;
    private String displayName;
    private boolean isActive;
    private LocalDate registrationDate;
}
