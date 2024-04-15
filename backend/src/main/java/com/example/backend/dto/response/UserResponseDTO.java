package com.example.backend.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponseDTO {

    private long id;
    private String email;
    private String name;
    private String lastName;
    private String displayName;
    private LocalDate registrationDate;

}
