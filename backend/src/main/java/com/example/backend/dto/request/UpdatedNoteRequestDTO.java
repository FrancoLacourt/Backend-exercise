package com.example.backend.dto.request;

import lombok.Data;

@Data
public class UpdatedNoteRequestDTO {

    private String title;
    private String description;
}