package com.example.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {

    private String token;
    private String email;
    private String id;
    private String name;
    private String lastName;
    private String userName;
}
